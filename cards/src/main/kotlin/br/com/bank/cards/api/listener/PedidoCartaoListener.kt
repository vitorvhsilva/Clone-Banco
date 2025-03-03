package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCartaoCompletoEventDTO
import br.com.bank.cards.api.listener.strategy.cartao.LimiteStrategy
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.service.CartaoService
import br.com.bank.cards.domain.utils.enums.TipoCartao
import br.com.bank.users.api.exception.CardAlreadyMadeException
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.exception.SegmentoNotAllowedException
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class PedidoCartaoListener (
    private val logger: Logger,
    private val cartaoRepository: CartaoRepository,
    private val catalogoRepository: CatalogoCartoesRepository,
    private val cartaoService: CartaoService
) {
    @KafkaListener(topics = ["pedido-cartoes-topic"], groupId = "pedidos-cartoes-consumer",
        containerFactory = "pedidoCartaoContainerFactory")
    fun processarPedido(event: PedidoCartaoCompletoEventDTO) {
        logger.info("Pedido de cartão de id ${event.idCartao} para o usuário ${event.idUsuario} recebido!")

        val cartaoCatalogo = catalogoRepository.findById(event.idCartao).orElseThrow({NotFoundException("Cartão não encontrado!")})

        if (!cartaoCatalogo.segmento.equals(event.segmento)) {
            logger.error("Segmento do usuário não permite esse cartão!")
            throw SegmentoNotAllowedException("Segmento do usuário não permite esse cartão!")
        }

        val cartoesDoUsuario = cartaoRepository.findAllByIdUsuario(event.idUsuario)

        if (!cartoesDoUsuario.none{it.idCatalogo == event.idCartao}) {
            logger.error("Esse cartão já foi feito!")
            throw CardAlreadyMadeException("Esse cartão já foi feito!")
        }

        val cartao = Cartao (
            idUsuario = event.idUsuario,
            idCatalogo = event.idCartao,
            nomeCartao = cartaoCatalogo.nome,
            nomeUsuario = event.nomeUsuario,
            agencia = event.agencia,
            conta = event.conta,
            bandeira = cartaoCatalogo.bandeira,
            segmento = event.segmento!!,
            tipoCartao = TipoCartao.CREDITO,
            faturas = emptyList()
        )

        cartaoService.injetarNumeroECodSeguranca(cartao)
        cartaoService.injetarLimite(cartao)

        cartaoRepository.save(cartao)
        logger.info("Cartão do usuário de id ${cartao.idUsuario} cadastrado com sucesso!")
    }
}