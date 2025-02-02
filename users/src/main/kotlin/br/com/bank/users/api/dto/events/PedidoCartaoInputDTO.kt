package br.com.bank.users.api.dto.events

data class PedidoCartaoInputDTO (
    val idCartao: Long,
    val idUsuario: String
)