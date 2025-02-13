package br.com.bank.cards.api.dto.output

import br.com.bank.cards.domain.utils.enums.StatusFatura
import java.math.BigDecimal

data class FaturaOutputDTO(
    val idFatura: String? = null,
    var valorFatura: BigDecimal,
    val mesAnoFatura: String,
    val status: StatusFatura
)
