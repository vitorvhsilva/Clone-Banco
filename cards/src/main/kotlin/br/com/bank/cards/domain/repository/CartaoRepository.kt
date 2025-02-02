package br.com.bank.cards.domain.repository

import br.com.bank.cards.domain.entity.Cartao
import org.springframework.data.jpa.repository.JpaRepository

interface CartaoRepository: JpaRepository<Cartao, String> {
    fun existsByNumeroCartao(numeroCartao: String): Boolean
    fun findAllByIdUsuario(idUsuario: String): MutableList<Cartao>
}