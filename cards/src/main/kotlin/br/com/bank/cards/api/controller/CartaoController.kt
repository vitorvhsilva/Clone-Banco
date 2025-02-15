package br.com.bank.cards.api.controller

import br.com.bank.cards.api.dto.output.CartaoOutputDTO
import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.api.dto.output.FaturaOutputDTO
import br.com.bank.cards.domain.service.CartaoService
import br.com.bank.cards.domain.utils.enums.Segmento
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("cartoes")
class CartaoController (
    private val cartaoService: CartaoService
) {

    @GetMapping("/segmentos/{segmento}") @Operation(description = "Obtem os cartões disponível pelo segmento do usuário")
    fun obterCartoesDisponiveisParaSegmento(@PathVariable segmento: Segmento): List<CatalogoCartaoOutputDTO>{
        return cartaoService.obterCartoesDisponiveisParaSegmento(segmento)
    }

    @GetMapping("/usuarios/{id}") @Operation(description = "Obtem os cartões pelo id do usuário")
    fun obterCartoesDisponiveisDoUsuario(@PathVariable id: String): List<CartaoOutputDTO>{
        return cartaoService.obterCartoesDisponiveisDoUsuario(id)
    }

    @GetMapping("/faturas/{id}") @Operation(description = "Obtem as faturas vinculadas a id do cartão")
    fun obterFaturaDoCartao(@PathVariable id: String): List<FaturaOutputDTO>{
        return cartaoService.obterFaturaDoCartao(id)
    }
}