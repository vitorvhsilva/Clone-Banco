package br.com.bank.cards.domain.service

import br.com.bank.cards.api.dto.output.CartaoOutputDTO
import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.api.dto.output.FaturaOutputDTO
import br.com.bank.cards.api.listener.strategy.cartao.LimiteStrategy
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.CatalogoCartoes
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.mappers.CartaoMapper
import br.com.bank.cards.domain.utils.mappers.CatalogoCartaoMapper
import br.com.bank.cards.domain.utils.mappers.FaturaMapper
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class CartaoService(
    private val catalogoMapper: CatalogoCartaoMapper,
    private val cartaoMapper: CartaoMapper,
    private val faturaMapper: FaturaMapper,
    private val catalogoRepository: CatalogoCartoesRepository,
    private val cartaoRepository: CartaoRepository,
    private val faturaRepository: FaturaRepository,
    private val strategys: List<LimiteStrategy>
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

    fun obterFaturaDoCartao(id: String): List<FaturaOutputDTO> {
        val faturas = faturaRepository.findAllByCartao_IdCartao(id)

        return faturas.map {f -> faturaMapper.entidadeParaOutput(f)}
    }

    fun injetarNumeroECodSeguranca(cartao: Cartao) {
        var numeroCartao: String
        var codigoSeguranca: String


        do {
            numeroCartao = (1..16).joinToString("") { Random.nextInt(10).toString() }
            codigoSeguranca = Random.nextInt(1000).toString().padStart(3, '0')
        } while (cartaoRepository.existsByNumeroCartao(numeroCartao))

        cartao.numeroCartao = numeroCartao
        cartao.codigoSeguranca = codigoSeguranca
    }

    fun injetarLimite(cartao: Cartao) {
        strategys.forEach{ s -> s.definirLimite(cartao) }
    }

}