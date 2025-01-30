package br.com.bank.users.api.dto.cards

import br.com.bank.users.domain.utils.enums.Bandeira
import br.com.bank.users.domain.utils.enums.Segmento

data class CatalogoCartaoOutputDTO(
    val id: Long,
    val nome: String,
    val bandeira: Bandeira,
    val segmento: Segmento
)
