package br.com.bank.users.api.http

import br.com.bank.users.api.dto.cards.CatalogoCartaoOutputDTO
import br.com.bank.users.domain.utils.enums.Segmento
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("ms-cards")
interface CartoesClient {
    @PostMapping("/cartoes/segmento")
    fun obterCartoesDisponiveisParaUsuario(@RequestBody segmento: Segmento): List<CatalogoCartaoOutputDTO>
}