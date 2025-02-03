package br.com.bank.users.domain.entity

import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusUsuario
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    var nome: String,
    @Column(unique = true)
    val email: String,
    var senha: String,
    @Column(unique = true)
    val cpf: String,
    @Enumerated(EnumType.STRING)
    var genero: Genero,
    @Embedded
    var endereco: Endereco? = null,
    var agencia: String = "",
    var conta: String = "",
    var rendaMensal: BigDecimal,
    var segmento: Segmento? = null,
    val saldoContaCorrente: BigDecimal,
    val dataNascimento: LocalDateTime,
    var dataCriacaoConta: LocalDateTime,
    @Enumerated(EnumType.STRING)
    var statusUsuario: StatusUsuario
)
