package br.com.bank.users.strategy

import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.service.strategy.*
import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusUsuario
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class SegmentoStrategyTest {

    private val strategys: List<SegmentoStrategy> = listOf(BaseSegmentoStrategy(), PlusSegmentoStrategy(),
        InfinitySegmentoStrategy(), PremiumSegmentoStrategy())

    private fun criarUsuario(rendaMensal: BigDecimal): Usuario {
        val usuario = Usuario(
            id = null,
            nome = "Vitor",
            email = "vitor@email.com",
            senha = "12345678",
            cpf = "12345678901",
            genero = Genero.MASCULINO,
            endereco = null,
            agencia = "",
            conta = "",
            rendaMensal = rendaMensal,
            segmento = null,
            saldoContaCorrente = BigDecimal(1),
            dataNascimento = LocalDateTime.of(2006, 1, 1, 1, 1),
            dataCriacaoConta = LocalDateTime.now(),
            statusUsuario = StatusUsuario.ATIVO
        )

        strategys.forEach {
                s -> s.injetarSegmento(usuario)
        }
        return usuario
    }

    @Test
    fun `Validar a injecao do Segmento Base`() {
        val usuario = criarUsuario(BigDecimal(1))

        assertEquals(usuario.segmento, Segmento.BASE)
    }

    @Test
    fun `Validar a injecao do Segmento Plus`() {
        val usuario = criarUsuario(BigDecimal(4000))

        assertEquals(usuario.segmento, Segmento.PLUS)
    }

    @Test
    fun `Validar a injecao do Segmento Premium`() {
        val usuario = criarUsuario(BigDecimal(10000))

        assertEquals(usuario.segmento, Segmento.PREMIUM)
    }

    @Test
    fun `Validar a injecao do Segmento Infinity`() {
        val usuario = criarUsuario(BigDecimal(20000))

        assertEquals(usuario.segmento, Segmento.INFINITY)
    }
}