package br.com.bank.cards.api.dto.events

import br.com.bank.cards.domain.utils.enums.StatusResposta
import java.math.BigDecimal

data class RespostaCreditoEventDTO (
    val idTransacao: String,
    val idUsuario: String,
    val valor: BigDecimal,
    val status: StatusResposta,
    val mensagem: String
)