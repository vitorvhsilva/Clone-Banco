package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCreditoEventDTO
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class PedidoCreditoListener(
    private val logger: Logger
) {
    @KafkaListener(topics = ["pedido-credito-topic"], groupId = "pedido-credito-consumer",
        containerFactory = "pedidoCreditoContainerFactory")
    fun processarPedidoCredito(event: PedidoCreditoEventDTO) {
        println(event)
    }
}