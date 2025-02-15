package br.com.bank.users.api.dto.input

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PagarFaturaInputDTO (
    @NotBlank
    val idUsuario: String,
    @NotBlank
    val idCartao: String,
    @NotNull
    val valorFatura: BigDecimal,
    @NotBlank
    val mesAnoFatura: String
)