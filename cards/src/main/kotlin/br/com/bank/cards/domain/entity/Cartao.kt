package br.com.bank.cards.domain.entity

import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.enums.TipoCartao
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.math.BigDecimal

@Entity
@Table(name = "tb_cartoes")
data class Cartao (
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val idCartao: String,
    val idUsuario: String,
    val nomeCartao: String,
    val nomeUsuario: String,
    @Size(min = 16, max = 16)
    val numeroCartao: String,
    @Size(min = 3, max = 3)
    val codigoSeguranca: String,
    val agencia: String,
    val conta: String,
    val limite: BigDecimal,
    @Enumerated(EnumType.STRING)
    val bandeira: Bandeira,
    @Enumerated(EnumType.STRING)
    val segmento: Segmento,
    @Enumerated(EnumType.STRING)
    val tipoCartao: TipoCartao
)