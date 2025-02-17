package br.com.bank.cards.api.dto.output

import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento

data class CatalogoCartaoOutputDTO(
    val id: Long,
    val nome: String,
    val bandeira: Bandeira,
    val segmento: Segmento
)
