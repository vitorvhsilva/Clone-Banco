package br.com.bank.cards.domain.service

import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.domain.entity.CatalogoCartoes
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.mappers.CatalogoCartaoMapper
import org.springframework.stereotype.Service

@Service
class CartaoService(
    private val catalogoMapper: CatalogoCartaoMapper,
    private val catalogoRepository: CatalogoCartoesRepository
) {
    fun obterCartoesDisponiveisParaUsuario(segmento: Segmento): List<CatalogoCartaoOutputDTO> {
        val cartoes: List<CatalogoCartoes> = catalogoRepository.findAllBySegmento(segmento)

        if (cartoes.isEmpty()) {
            throw RuntimeException("Lista de cartões vazia!")
        }

        val cartoesPegos: List<CatalogoCartaoOutputDTO> = cartoes.map{
            c -> catalogoMapper.entidadeParaOutput(c)
        }

        println(cartoesPegos)
        return cartoesPegos
    }

}