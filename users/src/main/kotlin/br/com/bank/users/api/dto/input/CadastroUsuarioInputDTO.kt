package br.com.bank.users.api.dto.input

import br.com.bank.users.domain.utils.enums.Genero
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDateTime

data class CadastroUsuarioInputDTO(
    @NotBlank
    val nome: String,
    @Email
    val email: String,
    @NotBlank @Size(min = 8)
    val senha: String,
    @Size(min = 11, max = 11)
    val cpf: String,
    @Enumerated(EnumType.STRING)
    val genero: Genero,
    val cep: String,
    @Positive
    val rendaMensal: BigDecimal,
    val saldoContaCorrente: BigDecimal,
    val dataNascimento: LocalDateTime,
)
