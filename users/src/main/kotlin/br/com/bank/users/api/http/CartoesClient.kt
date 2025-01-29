package br.com.bank.users.api.http

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient("ms-cards")
interface CartoesClient {
    @GetMapping("/cartoes")
    fun obterCartoesDisponiveisParaUsuario()
}