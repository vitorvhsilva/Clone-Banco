package br.com.bank.users.api.dto.output

import br.com.bank.users.domain.utils.enums.Genero
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

data class ObterUsuarioDTO(
    val nome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    @Enumerated(EnumType.STRING)
    val genero: Genero,
    var agencia: String = "",
    var conta: String = "",
    val dataNascimento: LocalDateTime
)
