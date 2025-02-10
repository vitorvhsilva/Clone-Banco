package br.com.bank.cards.domain.utils.mappers

import br.com.bank.cards.api.dto.output.FaturaOutputDTO
import br.com.bank.cards.domain.entity.Fatura
import org.springframework.stereotype.Component

@Component
class FaturaMapperImpl: FaturaMapper {
    override fun entidadeParaOutput(fatura: Fatura): FaturaOutputDTO {
        return FaturaOutputDTO (
            id = fatura.idFatura,
            valorFatura = fatura.valorFatura,
            mesAnoFatura = fatura.mesAnoFatura,
            status = fatura.status
        )
    }
}