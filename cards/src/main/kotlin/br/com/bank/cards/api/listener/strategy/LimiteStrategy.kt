package br.com.bank.cards.api.listener.strategy

import br.com.bank.cards.domain.entity.Cartao

interface LimiteStrategy {
    fun definirLimite(cartao: Cartao)
}