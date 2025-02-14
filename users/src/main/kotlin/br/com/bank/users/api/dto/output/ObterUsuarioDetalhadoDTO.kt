package br.com.bank.users.api.dto.output

import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusUsuario
import java.math.BigDecimal
import java.time.LocalDateTime

data class ObterUsuarioDetalhadoDTO(
    val id: String? = null,
    val nome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    val genero: Genero,
    var agencia: String = "",
    var conta: String = "",
    val rendaMensal: BigDecimal,
    val segmento: Segmento? = null,
    val saldoContaCorrente: BigDecimal,
    val dataNascimento: LocalDateTime,
    var dataCriacaoConta: LocalDateTime,
    var statusUsuario: StatusUsuario
)
