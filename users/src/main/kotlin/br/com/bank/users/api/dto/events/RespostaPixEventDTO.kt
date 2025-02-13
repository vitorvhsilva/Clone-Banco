package br.com.bank.users.api.dto.events

import br.com.bank.users.domain.utils.enums.StatusResposta

data class RespostaPixEventDTO (
    val idTransacao: String,
    val status: StatusResposta,
    val mensagem: String
)