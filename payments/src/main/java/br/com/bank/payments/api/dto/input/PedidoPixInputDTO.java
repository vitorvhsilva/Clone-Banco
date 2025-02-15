package br.com.bank.payments.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PedidoPixInputDTO (
    @NotBlank
    String idUsuari,
    @NotNull
    BigDecimal valor,
    @NotBlank
    String chavePix
) {}
