package br.com.bank.cards.api.dto.events

import java.math.BigDecimal

data class PedidoCreditoEventDTO  (
    val idTransacao: String,
    val idUsuario: String,
    val valor: BigDecimal,
    val qtdParcelas: Int,
    val chaveEstabelecimentoComercial: String
)