package br.com.bank.users.api.dto.input

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class PedidoCartaoInputDTO (
    @NotNull
    val idCartao: Long,
    @NotBlank
    val idUsuario: String
)