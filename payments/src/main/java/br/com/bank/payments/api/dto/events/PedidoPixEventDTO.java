package br.com.bank.payments.api.dto.events;

import java.math.BigDecimal;


public record PedidoPixEventDTO (
    String idTransacao,
    String idUsuario,
    BigDecimal valor,
    String chavePix
) {}
