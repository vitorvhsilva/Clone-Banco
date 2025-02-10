package br.com.bank.cards.api.controller

import br.com.bank.cards.api.dto.output.CartaoOutputDTO
import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.domain.service.CartaoService
import br.com.bank.cards.domain.utils.enums.Segmento
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("cartoes")
class CartaoController (
    private val cartaoService: CartaoService
) {

    @GetMapping("/segmentos/{segmento}")
    fun obterCartoesDisponiveisParaSegmento(@PathVariable segmento: Segmento): List<CatalogoCartaoOutputDTO>{
        return cartaoService.obterCartoesDisponiveisParaSegmento(segmento)
    }

    @GetMapping("/usuarios/{id}")
    fun obterCartoesDisponiveisDoUsuario(@PathVariable id: String): List<CartaoOutputDTO>{
        return cartaoService.obterCartoesDisponiveisDoUsuario(id)
    }
    
}