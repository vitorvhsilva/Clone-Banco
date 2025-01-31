package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCartaoDTO
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class PedidoCartaoListener () {
    @KafkaListener(topics = ["pedidos-cartoes-topic"], groupId = "processar-pedido")
    fun processarPedido(dto: PedidoCartaoDTO) {
        println(dto)
    }
}