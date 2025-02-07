package br.com.bank.cards.api.dto.events

import br.com.bank.cards.domain.utils.enums.Segmento

data class PedidoCartaoCompletoEventDTO (
    val idCartao: Long,
    val idUsuario: String,
    var nomeUsuario: String,
    var agencia: String = "",
    var conta: String = "",
    var segmento: Segmento? = null
)