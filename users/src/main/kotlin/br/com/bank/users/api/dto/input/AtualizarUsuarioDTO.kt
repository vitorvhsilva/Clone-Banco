package br.com.bank.users.api.dto.input

import br.com.bank.users.domain.utils.enums.Genero
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

data class AtualizarUsuarioDTO(
    val id: String,
    val nome: String,
    val senha: String,
    @Enumerated(EnumType.STRING)
    val genero: Genero,
    val rendaMensal: BigDecimal
)
