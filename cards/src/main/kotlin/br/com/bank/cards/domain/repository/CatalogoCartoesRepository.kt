package br.com.bank.cards.domain.repository

import br.com.bank.cards.domain.entity.CatalogoCartoes
import br.com.bank.cards.domain.utils.enums.Segmento
import org.springframework.data.jpa.repository.JpaRepository

interface CatalogoCartoesRepository: JpaRepository<CatalogoCartoes, Long> {
    fun findAllBySegmento(segmento: Segmento): List<CatalogoCartoes>
}