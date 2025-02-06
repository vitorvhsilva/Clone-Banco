package br.com.bank.users.api.dto.events

import java.math.BigDecimal

data class PedidoPixEventDTO (
    val idUsuario: String,
    val valor: BigDecimal,
    val chavePix: String
)