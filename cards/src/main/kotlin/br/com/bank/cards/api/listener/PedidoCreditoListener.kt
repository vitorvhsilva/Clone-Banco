package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCreditoEventDTO
import br.com.bank.cards.api.exception.LimitException
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.Fatura
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.users.api.exception.NotFoundException
import jakarta.persistence.*
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class PedidoCreditoListener(
    private val logger: Logger,
    private val cartaoRepository: CartaoRepository,
    private val faturaRepository: FaturaRepository
) {
    @KafkaListener(topics = ["pedido-credito-topic"], groupId = "pedido-credito-consumer",
        containerFactory = "pedidoCreditoContainerFactory")
    fun processarPedidoCredito(event: PedidoCreditoEventDTO) {
        val cartao = cartaoRepository.findById(event.idCartao).orElseThrow({NotFoundException("Cartão não encontrado!")})

        if (event.valor > cartao.limite) {
            logger.error("Valor do crédito maior que o limite!")
            throw LimitException("Valor do crédito maior que o limite!")
        }

        val mesAnoAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"))

        val fatura = faturaRepository.findByMesAnoFatura(mesAnoAtual)

        if (fatura.isPresent) {
            // consolidarFatura(fatura.get())
        } else {
            criarFatura(cartao, event, mesAnoAtual)
        }

    }

    private fun consolidarFatura (fatura: Fatura, event: PedidoCreditoEventDTO) {

    }

    private fun criarFatura(cartao: Cartao, event: PedidoCreditoEventDTO, mesAnoAtual: String) {
        val fatura = Fatura (
            cartao = cartao,
            valorFatura = event.valor,
            mesAnoFatura = mesAnoAtual
        )

        faturaRepository.save(fatura)

        logger.info("Fatura para usuário de id ${event.idUsuario} o mês ${mesAnoAtual} criada!")
    }
}