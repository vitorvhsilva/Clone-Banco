package br.com.bank.cards.api.dto.events

import br.com.bank.cards.domain.utils.enums.StatusResposta

data class RespostaCreditoEventDTO (
    val idTransacao: String,
    val status: StatusResposta,
    val mensagem: String
)