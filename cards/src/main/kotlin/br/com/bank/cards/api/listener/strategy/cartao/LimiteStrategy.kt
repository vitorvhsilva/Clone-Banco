package br.com.bank.cards.api.listener.strategy.cartao

import br.com.bank.cards.domain.entity.Cartao

interface LimiteStrategy {
    fun definirLimite(cartao: Cartao)
}