package br.com.bank.users.api.dto.events

import br.com.bank.users.domain.utils.enums.StatusTransacao
import java.math.BigDecimal

data class PagarFaturaEventDTO(
    val idUsuario: String,
    val idCartao: String,
    val valorFatura: BigDecimal,
    val mesAnoFatura: String,
    var status: StatusTransacao,
    val mensagem: String
)
