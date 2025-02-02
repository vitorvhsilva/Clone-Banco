package br.com.bank.users.api.http

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient("https://viacep.com.br/ws")
interface ViaCepClient {
    @GetMapping("/{cep}/json/")
    fun obterEnderecoDoUsuario(@PathVariable cep: String)
}