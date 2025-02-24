package br.com.bank.users.listener

import br.com.bank.users.api.dto.events.PagarFaturaEventDTO
import br.com.bank.users.api.listener.RespostaFaturaListener
import br.com.bank.users.domain.entity.Endereco
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusTransacao
import br.com.bank.users.domain.utils.enums.StatusUsuario
import org.hibernate.mapping.UnionSubclass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class RespostaFaturaListenerTest {
    @Mock
    private lateinit var usuarioRepository: UsuarioRepository

    @Mock
    private lateinit var logger: Logger

    @InjectMocks
    private lateinit var respostaFaturaListener: RespostaFaturaListener

    private lateinit var usuario: Usuario

    private lateinit var pagarFaturaEventDTO: PagarFaturaEventDTO

    @BeforeEach
    fun setUp() {
        val id = UUID.randomUUID().toString()

        usuario = Usuario (
            id = id,
            nome = "Vitor Hugo",
            email = "vitor@email.com",
            senha = "12345678",
            cpf = "12345678912",
            genero = Genero.MASCULINO,
            saldoContaCorrente = BigDecimal(0),
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

        pagarFaturaEventDTO = PagarFaturaEventDTO (
            idUsuario = id,
            idCartao = "",
            valorFatura = BigDecimal.ZERO,
            mesAnoFatura = "",
            status = StatusTransacao.EM_PROCESSAMENTO,
            mensagem = ""
        )
    }

    @Test
    fun `Processar pagamento da fatura deve aumentar saldo se status for invalido`() {
        val event = PagarFaturaEventDTO(
            idUsuario = "usuario-id",
            idCartao = "cartao-id",
            valorFatura = BigDecimal(100),
            mesAnoFatura = "10/2023",
            status = StatusTransacao.INVALIDA,
            mensagem = "Erro no pagamento"
        )

        val usuario = this.usuario
        `when`(usuarioRepository.findById("usuario-id")).thenReturn(Optional.of(usuario))

        respostaFaturaListener.processarPagamentoFatura(event)

        verify(usuarioRepository, times(1)).findById("usuario-id")
        verify(logger, times(1)).info("Resposta de fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} recebido!")
        verify(logger, times(1)).error("Erro ao pagar a fatura, a mensagem foi ${event.mensagem}")
        verify(logger, never()).info("Fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} paga com sucesso!")
        assertEquals(BigDecimal(100), usuario.saldoContaCorrente)
    }

    @Test
    fun `Processar pagamento da fatura nao deve aumentar saldo se status for invalido`() {
        val event = PagarFaturaEventDTO(
            idUsuario = "usuario-id",
            idCartao = "cartao-id",
            valorFatura = BigDecimal(100),
            mesAnoFatura = "10/2023",
            status = StatusTransacao.VALIDA,
            mensagem = "Pagamento válido"
        )

        val usuario = this.usuario
        `when`(usuarioRepository.findById("usuario-id")).thenReturn(Optional.of(usuario))

        respostaFaturaListener.processarPagamentoFatura(event)

        verify(usuarioRepository, times(1)).findById("usuario-id")
        verify(logger, times(1)).info("Resposta de fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} recebido!")
        verify(logger, never()).error("Erro ao pagar a fatura, a mensagem foi ${event.mensagem}")
        verify(logger, times(1)).info("Fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} paga com sucesso!")
        assertEquals(BigDecimal(0), usuario.saldoContaCorrente)
    }
}