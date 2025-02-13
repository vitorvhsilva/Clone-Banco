package br.com.bank.users.api.listener

import br.com.bank.users.api.dto.events.PedidoPixEventDTO
import br.com.bank.users.api.dto.events.RespostaPixEventDTO
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.utils.enums.StatusResposta
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class PedidoPixListener (
    private val usuarioRepository: UsuarioRepository,
    private val logger: Logger,
    private val respostaPixKafkaTemplate: KafkaTemplate<String, RespostaPixEventDTO>
) {
    @Transactional
    @KafkaListener(topics = ["pedido-pix-topic"], groupId = "pedido-pix-consumer",
        containerFactory = "pedidoPixContainerFactory")
    fun processarPix(event: PedidoPixEventDTO) {
        logger.info("Pedido de pix do usuário ${event.idUsuario} para a chave pix ${event.chavePix} recebido!")

        if (!usuarioRepository.existsById(event.idUsuario)) {
            logger.error("Usuário não encontrado!")

            val pixAceito = RespostaPixEventDTO (
                idTransacao = event.idTransacao,
                status = StatusResposta.INVALIDO,
                mensagem = "Usuário não encontrado!"
            )

            respostaPixKafkaTemplate.send("resposta-pix-topic", pixAceito.idTransacao, pixAceito)
            return
        }

        if (!usuarioRepository.existsByEmailOrCpf(event.chavePix, event.chavePix)){
            logger.error("Recebedor do Pix não encontrado!")

            val pixAceito = RespostaPixEventDTO (
                idTransacao = event.idTransacao,
                status = StatusResposta.INVALIDO,
                mensagem = "Recebedor do Pix não encontrado!"
            )

            respostaPixKafkaTemplate.send("resposta-pix-topic", pixAceito.idTransacao, pixAceito)
            return
        }

        val usuarioFazedor: Usuario = usuarioRepository.findById(event.idUsuario).get()
        val usuarioRecebedor: Usuario = usuarioRepository.findByEmailOrCpf(event.chavePix, event.chavePix).get(0)

        usuarioFazedor.diminuirSaldo(event.valor)
        usuarioRecebedor.aumentarSaldo(event.valor)

        logger.info("Pix do usuário ${event.idUsuario} para a chave pix ${event.chavePix} feito com sucesso!")

        val pixAceito = RespostaPixEventDTO (
            idTransacao = event.idTransacao,
            status = StatusResposta.VALIDO,
            mensagem = "O pix foi aceito com sucesso!"
        )

        respostaPixKafkaTemplate.send("resposta-pix-topic", pixAceito.idTransacao, pixAceito)

        logger.info("Mensagem de pix aceito enviada!")
    }
}