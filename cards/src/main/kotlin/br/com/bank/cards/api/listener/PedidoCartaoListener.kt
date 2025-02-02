package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCartaoCompletoDTO
import br.com.bank.cards.api.listener.strategy.LimiteStrategy
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.CatalogoCartoes
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.enums.TipoCartao
import br.com.bank.users.api.exception.NotFoundException
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.random.Random

@Service
class PedidoCartaoListener (
    private val logger: Logger,
    private val cartaoRepository: CartaoRepository,
    private val catalogoRepository: CatalogoCartoesRepository,
    private val strategys: List<LimiteStrategy>
) {
    @KafkaListener(topics = ["pedidos-cartoes-topic"], groupId = "processar-pedido")
    fun processarPedido(dto: PedidoCartaoCompletoDTO) {
        println(dto)
        logger.info("Pedido de cartão para o usuário ${dto.idUsuario} recebido!")

        val cartaoCatalogo = catalogoRepository.findById(dto.idCartao).orElseThrow({NotFoundException("Cartão não encontrado!")})

        val cartao = Cartao (
            idUsuario = dto.idUsuario,
            nomeCartao = cartaoCatalogo.nome,
            nomeUsuario = dto.nomeUsuario,
            agencia = dto.agencia,
            conta = dto.conta,
            bandeira = cartaoCatalogo.bandeira,
            segmento = dto.segmento!!,
            tipoCartao = TipoCartao.CREDITO
        )

        injetarNumeroECodSeguranca(cartao)
        injetarLimite(cartao)

        cartaoRepository.save(cartao)
        logger.info("Cartão do usuário de id ${cartao.idUsuario} cadastrado com sucesso!")
    }

    private fun injetarNumeroECodSeguranca(cartao: Cartao) {
        var numeroCartao: String
        var codigoSeguranca: String


        do {
            numeroCartao = (1..16).joinToString("") { Random.nextInt(10).toString() }
            codigoSeguranca = Random.nextInt(1000).toString().padStart(3, '0')
        } while (cartaoRepository.existsByNumeroCartao(numeroCartao))

        cartao.numeroCartao = numeroCartao
        cartao.codigoSeguranca = codigoSeguranca
    }

    private fun injetarLimite(cartao: Cartao) {
        strategys.forEach{ s -> s.definirLimite(cartao) }
    }
}