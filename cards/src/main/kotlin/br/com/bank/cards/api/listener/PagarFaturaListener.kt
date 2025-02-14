package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PagarFaturaEventDTO
import br.com.bank.cards.api.dto.events.PedidoCartaoCompletoEventDTO
import br.com.bank.cards.api.listener.strategy.cartao.LimiteStrategy
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.cards.domain.utils.enums.StatusFatura
import br.com.bank.cards.domain.utils.enums.TipoCartao
import br.com.bank.users.api.exception.CardAlreadyMadeException
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.exception.SegmentoNotAllowedException
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class PagarFaturaListener (
    private val logger: Logger,
    private val faturaRepository: FaturaRepository
) {
    @KafkaListener(topics = ["pagar-fatura-topic"], groupId = "pagar-fatura-consumer",
        containerFactory = "pagarFaturaContainerFactory")
    fun processarPedido(event: PagarFaturaEventDTO) {
        logger.info("Pedido de pagar fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} recebido!")

        val fatura = faturaRepository.findByMesAnoFatura(event.mesAnoFatura)

        if (fatura.isEmpty) {
            logger.info("A fatura desse mês não existe!")
            // colocar o envio da mensagem

            return
        }

        val faturaPega = fatura.get()

        if (!faturaPega.valorFatura.equals(event.valorFatura)) {
            logger.info("Valor da fatura diferente do que deveria!")
            // colocar o envio da mensagem

            return
        }

        faturaPega.status = StatusFatura.PAGA
        
    }

}