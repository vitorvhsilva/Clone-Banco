package br.com.bank.users.api.dto.events

import br.com.bank.users.domain.utils.enums.StatusResposta
import java.math.BigDecimal

data class RespostaPixEventDTO (
    val idTransacao: String,
    val idUsuario: String,
    val valor: BigDecimal,
    val status: StatusResposta,
    val mensagem: String
)