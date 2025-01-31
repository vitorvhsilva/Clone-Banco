package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCartaoDTO
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class PedidoCartaoListener (
    private val logger: Logger
) {
    @KafkaListener(topics = ["pedidos-cartoes-topic"], groupId = "processar-pedido")
    fun processarPedido(dto: PedidoCartaoDTO) {
        println(dto)
        logger.info("Pedido de cartão para o usuário ${dto.idUsuario} recebido!")

    }
}