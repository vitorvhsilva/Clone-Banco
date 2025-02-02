package br.com.bank.cards.domain.utils.mappers

import br.com.bank.cards.api.dto.output.CartaoOutputDTO
import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.CatalogoCartoes

interface CartaoMapper {
    fun entidadeParaOutput(cartao: Cartao): CartaoOutputDTO
}