package br.com.bank.users.api.dto.input

import java.math.BigDecimal

data class PagarFaturaInputDTO (
    val idUsuario: String,
    val valorFatura: BigDecimal,
    val mesAnoFatura: String
)