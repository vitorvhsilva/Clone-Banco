package br.com.bank.users.domain.entity

import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.StatusUsuario
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    val nome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    @Enumerated(EnumType.STRING)
    val genero: Genero,
    var agencia: String = "",
    var conta: String = "",
    val dataNascimento: LocalDateTime,
    var dataCriacaoConta: LocalDateTime,
    @Enumerated(EnumType.STRING)
    var statusUsuario: StatusUsuario
) {
}
