package br.com.bank.cards.domain.utils.mappers

import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.domain.entity.CatalogoCartoes
import org.springframework.stereotype.Component

@Component
class CatalogoCartaoMapperImpl: CatalogoCartaoMapper {
    override fun entidadeParaOutput(cartao: CatalogoCartoes): CatalogoCartaoOutputDTO {
        return CatalogoCartaoOutputDTO(
            id = cartao.id,
            nome = cartao.nome,
            segmento = cartao.segmento,
            bandeira = cartao.bandeira
        )
    }
}