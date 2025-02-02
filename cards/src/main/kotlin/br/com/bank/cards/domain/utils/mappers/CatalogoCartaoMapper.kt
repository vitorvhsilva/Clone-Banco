package br.com.bank.cards.domain.utils.mappers

import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.domain.entity.CatalogoCartoes

interface CatalogoCartaoMapper {
    fun entidadeParaOutput(cartao: CatalogoCartoes): CatalogoCartaoOutputDTO
}