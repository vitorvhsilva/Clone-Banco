package br.com.bank.cards.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("cartoes")
class CartaoController {

    @GetMapping
    fun teste() {
        println("Teste!")
    }
}