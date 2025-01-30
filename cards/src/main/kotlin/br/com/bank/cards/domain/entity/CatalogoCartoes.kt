package br.com.bank.cards.domain.entity

import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import jakarta.persistence.*

@Entity
@Table(name = "tb_catalogo")
data class CatalogoCartoes (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val nome: String,
    @Enumerated(EnumType.STRING)
    val bandeira: Bandeira,
    @Enumerated(EnumType.STRING)
    val segmento: Segmento
)