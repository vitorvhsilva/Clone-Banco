package br.com.bank.users.api.dto.events

import br.com.bank.users.domain.utils.enums.Segmento

data class PedidoCartaoCompletoDTO (
    val idCartao: Long,
    val idUsuario: String,
    var nomeUsuario: String,
    var agencia: String = "",
    var conta: String = "",
    var segmento: Segmento? = null
)