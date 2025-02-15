package br.com.bank.payments.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record PedidoCreditoInputDTO (
    @NotBlank
    String idUsuario,
    @NotBlank
    String idCartao,
    @NotNull
    BigDecimal valor,
    @NotNull
    Integer qtdParcelas,
    @NotBlank
    String chaveEstabelecimentoComercial
) {}
