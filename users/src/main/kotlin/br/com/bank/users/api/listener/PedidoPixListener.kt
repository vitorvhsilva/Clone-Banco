package br.com.bank.users.api.listener

import br.com.bank.users.api.dto.events.PedidoPixEventDTO
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class PedidoPixListener {
    @KafkaListener(topics = ["pedido-pix-topic"], groupId = "pedido-pix-consumer",
        containerFactory = "pedidoPixContainerFactory")
    fun processarPix(event: PedidoPixEventDTO) {
        println(event)
    }
}