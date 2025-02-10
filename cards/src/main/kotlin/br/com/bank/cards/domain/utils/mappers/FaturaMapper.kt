package br.com.bank.cards.domain.utils.mappers

import br.com.bank.cards.api.dto.output.FaturaOutputDTO
import br.com.bank.cards.domain.entity.Fatura

interface FaturaMapper {
    fun entidadeParaOutput(fatura: Fatura): FaturaOutputDTO
}