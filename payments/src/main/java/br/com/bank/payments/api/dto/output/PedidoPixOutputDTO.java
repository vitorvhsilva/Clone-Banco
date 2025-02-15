package br.com.bank.payments.api.dto.output;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;

import java.math.BigDecimal;

public record PedidoPixOutputDTO (
    String idTransacao,
    String idUsuario,
    BigDecimal valor,
    StatusTransacao status,
    String chavePix
) {}
