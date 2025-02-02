package br.com.bank.cards.domain.service

import br.com.bank.cards.api.dto.output.CartaoOutputDTO
import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.domain.entity.CatalogoCartoes
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.mappers.CartaoMapper
import br.com.bank.cards.domain.utils.mappers.CatalogoCartaoMapper
import org.springframework.stereotype.Service

@Service
class CartaoService(
    private val catalogoMapper: CatalogoCartaoMapper,
    private val catalogoRepository: CatalogoCartoesRepository,
    private val cartaoRepository: CartaoRepository,
    private val cartaoMapper: CartaoMapper
) {
    fun obterCartoesDisponiveisParaSegmento(segmento: Segmento): List<CatalogoCartaoOutputDTO> {
        val cartoes: List<CatalogoCartoes> = catalogoRepository.findAllBySegmento(segmento)

        if (cartoes.isEmpty()) {
            throw RuntimeException("Lista de cartões vazia!")
        }

        val cartoesPegos: List<CatalogoCartaoOutputDTO> = cartoes.map{
            c -> catalogoMapper.entidadeParaOutput(c)
        }

        return cartoesPegos
    }

    fun obterCartoesDisponiveisDoUsuario(id: String): List<CartaoOutputDTO> {
        val cartoes = cartaoRepository.findAllByIdUsuario(id)

        return cartoes.map {c -> cartaoMapper.entidadeParaOutput(c)}

    }

}