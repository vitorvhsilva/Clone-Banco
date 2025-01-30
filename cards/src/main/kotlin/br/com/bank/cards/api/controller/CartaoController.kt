package br.com.bank.cards.api.controller

import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.domain.service.CartaoService
import br.com.bank.cards.domain.utils.enums.Segmento
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("cartoes")
class CartaoController (
    private val cartaoService: CartaoService
) {

    @PostMapping("/segmento")
    fun obterCartoesDisponiveisParaUsuario(@RequestBody segmento: Segmento): List<CatalogoCartaoOutputDTO>{
        return cartaoService.obterCartoesDisponiveisParaUsuario(segmento)
    }
}