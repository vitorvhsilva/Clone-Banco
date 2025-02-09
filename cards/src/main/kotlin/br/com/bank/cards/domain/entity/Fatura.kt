package br.com.bank.cards.domain.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "tb_fatura")
data class Fatura(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @ManyToOne @JoinColumn(name = "id_cartao")
    val cartao: Cartao,
    val valorFatura: BigDecimal,
    @Column(unique = true)
    val mesAnoFatura: String
)
