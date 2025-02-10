package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCreditoEventDTO
import br.com.bank.cards.api.exception.LimitException
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.Fatura
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.users.api.exception.NotFoundException
import jakarta.persistence.*
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class PedidoCreditoListener(
    private val logger: Logger,
    private val cartaoRepository: CartaoRepository,
    private val faturaRepository: FaturaRepository
) {
    @Transactional
    @KafkaListener(topics = ["pedido-credito-topic"], groupId = "pedido-credito-consumer",
        containerFactory = "pedidoCreditoContainerFactory")
    fun processarPedidoCredito(event: PedidoCreditoEventDTO) {
        val cartao = cartaoRepository.findById(event.idCartao).orElseThrow({NotFoundException("Cartão não encontrado!")})

        if (event.valor > cartao.limite) {
            logger.error("Valor do crédito maior que o limite!")
            throw LimitException("Valor do crédito maior que o limite!")
        }

        cartao.limite -= event.valor // descontando o valor no limite

        val valorFatura = event.valor / event.qtdParcelas.toBigDecimal()

        val mesesFatura: ArrayList<String> = ArrayList()

        for (num in 0..<event.qtdParcelas) {
            val mesAnoFatura = LocalDate.now().plusMonths(num.toLong()).format(DateTimeFormatter.ofPattern("MM/yyyy"))
            mesesFatura.add(mesAnoFatura)
        }

        for (mesFatura in mesesFatura) {
            val fatura = faturaRepository.findByMesAnoFatura(mesFatura)

            if (fatura.isPresent) {
                consolidarFatura(fatura.get(), event, mesFatura, valorFatura)
            } else {
                criarFatura(cartao, event, mesFatura, valorFatura)
            }
        }

        logger.info("Pedido de crédito processado!")
    }

    private fun consolidarFatura (fatura: Fatura, event: PedidoCreditoEventDTO, mesAnoAtual: String, valorFatura: BigDecimal) {
        fatura.valorFatura += valorFatura

        logger.info("""
            Fatura consolidada! 
            idUsuário: ${event.idUsuario}
            idCartão: ${fatura.cartao.idCartao}
            MêsAno: ${mesAnoAtual}""")
    }

    private fun criarFatura(cartao: Cartao, event: PedidoCreditoEventDTO, mesAnoAtual: String, valorFatura: BigDecimal) {
        val fatura = Fatura (
            cartao = cartao,
            valorFatura = valorFatura,
            mesAnoFatura = mesAnoAtual
        )

        faturaRepository.save(fatura)

        logger.info("""
            Fatura criada! 
            idUsuário: ${event.idUsuario}
            idCartão: ${fatura.cartao.idCartao}
            MêsAno: ${mesAnoAtual}""")
    }
}