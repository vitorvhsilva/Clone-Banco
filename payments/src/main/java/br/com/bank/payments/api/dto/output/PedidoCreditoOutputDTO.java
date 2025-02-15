package br.com.bank.payments.api.dto.output;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;

import java.math.BigDecimal;

public record PedidoCreditoOutputDTO (
    String idTransacao,
    String idUsuario,
    String idCartao,
    BigDecimal valor,
    StatusTransacao status,
    Integer qtdParcelas,
    String chaveEstabelecimentoComercial
) {}
