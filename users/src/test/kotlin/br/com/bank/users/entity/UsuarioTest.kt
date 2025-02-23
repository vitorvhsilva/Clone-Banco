package br.com.bank.users.entity

import br.com.bank.users.domain.entity.Endereco
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusUsuario
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UsuarioTest {
    private val usuario = Usuario(
        id = UUID.randomUUID().toString(),
        nome = "Vitor Hugo",
        email = "vitor@email.com",
        senha = "12345678",
        cpf = "12345678912",
        genero = Genero.MASCULINO,
        saldoContaCorrente = BigDecimal(20000),
        rendaMensal = BigDecimal(5000),
        dataNascimento = LocalDateTime.parse("2006-03-28T15:30:00"),
        statusUsuario = StatusUsuario.ATIVO,
        dataCriacaoConta = LocalDateTime.now(),
        agencia = "001",
        segmento = Segmento.PLUS,
        conta = "12345-6",
        endereco = Endereco(
            cep = "",
            logradouro = "",
            bairro = "",
            uf = "",
            estado = "",
            regiao = ""
        )
    )

    @Test
    fun `Testar aumentar saldo`() {
        usuario.saldoContaCorrente = BigDecimal(0)

        usuario.aumentarSaldo(BigDecimal(1))

        assertEquals(BigDecimal(1), usuario.saldoContaCorrente)
    }

    @Test
    fun `Testar diminuir saldo`() {
        usuario.saldoContaCorrente = BigDecimal(10)

        usuario.diminuirSaldo(BigDecimal(1))

        assertEquals(BigDecimal(9), usuario.saldoContaCorrente)
    }

    @Test
    fun `Testar adicionar valor negativo ao saldo`() {
        assertFailsWith<IllegalArgumentException> {
            usuario.aumentarSaldo(BigDecimal(-1))
        }
    }

    @Test
    fun `Testar diminuir saldo com um zero ou retirar mais que a conta corrente`() {
        usuario.saldoContaCorrente = BigDecimal(0)

        assertEquals(BigDecimal(0), usuario.saldoContaCorrente)

        assertFailsWith<IllegalArgumentException> {
            usuario.diminuirSaldo(BigDecimal(0))
        }

        assertFailsWith<IllegalStateException> {
            usuario.diminuirSaldo(BigDecimal(10))
        }
    }
}