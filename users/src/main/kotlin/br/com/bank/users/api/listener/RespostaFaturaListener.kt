package br.com.bank.users.api.listener

import br.com.bank.users.api.dto.events.PagarFaturaEventDTO
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.utils.enums.StatusTransacao
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class RespostaFaturaListener (
    private val usuarioRepository: UsuarioRepository,
    private val logger: Logger,
) {
    @Transactional
    @KafkaListener(topics = ["resposta-fatura-topic"], groupId = "resposta-fatura-consumer",
        containerFactory = "respostaFaturaContainerFactory")
    fun processarPagamentoFatura(event: PagarFaturaEventDTO) {
        val usuario = usuarioRepository.findById(event.idUsuario).get()

        logger.info("Resposta de fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} recebido!")

        if (event.status.equals(StatusTransacao.INVALIDA)){
            usuario.aumentarSaldo(event.valorFatura)
            logger.error("Erro ao pagar a fatura, a mensagem foi ${event.mensagem}")
            return
        }

        logger.info("Fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} paga com sucesso!")

    }
}