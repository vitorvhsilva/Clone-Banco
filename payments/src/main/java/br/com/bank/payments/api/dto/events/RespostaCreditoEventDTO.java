package br.com.bank.payments.api.dto.events;

import br.com.bank.payments.domain.utils.enums.StatusResposta;


public record RespostaCreditoEventDTO (
    String idTransacao,
    StatusResposta status,
    String mensagem
) {}

