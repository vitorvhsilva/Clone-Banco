package br.com.bank.cards.domain.repository

import br.com.bank.cards.domain.entity.Fatura
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FaturaRepository: JpaRepository<Fatura, String> {
    fun findByMesAnoFatura(mesAnoFatura: String): Optional<Fatura>
}