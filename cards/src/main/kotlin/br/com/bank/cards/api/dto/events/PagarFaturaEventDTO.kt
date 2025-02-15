package br.com.bank.cards.api.dto.events

import br.com.bank.cards.domain.utils.enums.StatusTransacao
import java.math.BigDecimal

data class PagarFaturaEventDTO(
    val idUsuario: String,
    val idCartao: String,
    val valorFatura: BigDecimal,
    val mesAnoFatura: String,
    var status: StatusTransacao,
    var mensagem: String
)
