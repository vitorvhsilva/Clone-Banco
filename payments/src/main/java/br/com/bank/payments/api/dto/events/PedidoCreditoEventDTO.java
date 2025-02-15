package br.com.bank.payments.api.dto.events;

import java.math.BigDecimal;

public record PedidoCreditoEventDTO (
    String idTransacao,
    String idUsuario,
    String idCartao,
    BigDecimal valor,
    Integer qtdParcelas,
    String chaveEstabelecimentoComercial
) { }
