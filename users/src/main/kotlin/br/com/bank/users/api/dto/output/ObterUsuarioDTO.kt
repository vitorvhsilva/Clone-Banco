package br.com.bank.users.api.dto.output

import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

data class ObterUsuarioDTO(
    val id: String? = null,
    val nome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    @Enumerated(EnumType.STRING)
    val genero: Genero,
    var agencia: String = "",
    var conta: String = "",
    val segmento: Segmento? = null,
    val dataNascimento: LocalDateTime
)
