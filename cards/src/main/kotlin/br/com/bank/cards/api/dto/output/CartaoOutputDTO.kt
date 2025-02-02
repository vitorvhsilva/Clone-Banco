package br.com.bank.cards.api.dto.output

import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.enums.TipoCartao
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal
import java.util.*

data class CartaoOutputDTO(
    val idCartao: UUID? = null,
    val idUsuario: String,
    val nomeCartao: String,
    val nomeUsuario: String,
    var numeroCartao: String = "",
    var codigoSeguranca: String = "",
    val agencia: String,
    val conta: String,
    var limite: BigDecimal = BigDecimal.ZERO,
    @Enumerated(EnumType.STRING)
    val bandeira: Bandeira,
    @Enumerated(EnumType.STRING)
    val segmento: Segmento,
    @Enumerated(EnumType.STRING)
    val tipoCartao: TipoCartao
)
