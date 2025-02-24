package br.com.bank.users.listener

import br.com.bank.users.api.dto.events.PedidoPixEventDTO
import br.com.bank.users.api.dto.events.RespostaPixEventDTO
import br.com.bank.users.api.listener.PedidoPixListener
import br.com.bank.users.domain.entity.Endereco
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusUsuario
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class PedidoPixListenerTest {

    @Mock
    private lateinit var usuarioRepository: UsuarioRepository

    @Mock
    private lateinit var logger: Logger

    @Mock
    private lateinit var respostaPixKafkaTemplate: KafkaTemplate<String, RespostaPixEventDTO>

    @InjectMocks
    private lateinit var pedidoPixListener: PedidoPixListener

    private lateinit var usuarioFazedor: Usuario
    private lateinit var usuarioRecebedor: Usuario
    private lateinit var event: PedidoPixEventDTO

    @BeforeEach
    fun setUp() {
        usuarioFazedor = Usuario (
            id = UUID.randomUUID().toString(),
            nome = "Vitor Hugo",
            email = "vitor@email.com",
            senha = "12345678",
            cpf = "12345678912",
            genero = Genero.MASCULINO,
            saldoContaCorrente = BigDecimal(100),
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

        usuarioRecebedor = Usuario (
            id = UUID.randomUUID().toString(),
            nome = "Vitor Hugo",
            email = "vitor2@email.com",
            senha = "12345678",
            cpf = "12345678913",
            genero = Genero.MASCULINO,
            saldoContaCorrente = BigDecimal(100),
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

        event = PedidoPixEventDTO(
            idTransacao = "",
            idUsuario = usuarioFazedor.id!!,
            valor = BigDecimal(10),
            chavePix = usuarioRecebedor.id!!
        )
    }

    @Test
    fun `Retornar mensagem de erro quando o usuario fazedor do pix nao for encontrado`(){
        `when`(usuarioRepository.existsById(usuarioFazedor.id!!)).thenReturn(false)

        pedidoPixListener.processarPix(event)

        verify(logger, times(1)).info("Pedido de pix do usuário ${event.idUsuario} para a chave pix ${event.chavePix} recebido!")
        verify(logger, times(1)).error("Usuário não encontrado!")
        verify(respostaPixKafkaTemplate, times(1)).send(eq("resposta-pix-topic"),
            eq(event.idTransacao), any())
    }

    @Test
    fun `Retornar mensagem de erro quando o usuario recebedor do pix nao for encontrado`(){
        `when`(usuarioRepository.existsById(usuarioFazedor.id!!)).thenReturn(true)
        `when`(usuarioRepository.existsByEmailOrCpf(usuarioRecebedor.id!!, usuarioRecebedor.id!!)).thenReturn(false)

        pedidoPixListener.processarPix(event)

        verify(logger, times(1)).info("Pedido de pix do usuário ${event.idUsuario} para a chave pix ${event.chavePix} recebido!")
        verify(logger, times(1)).error("Recebedor do Pix não encontrado!")
        verify(respostaPixKafkaTemplate, times(1)).send(eq("resposta-pix-topic"),
            eq(event.idTransacao), any())
    }

    @Test
    fun `Verificar saldo apos receber a transacao`(){
        `when`(usuarioRepository.existsById(usuarioFazedor.id!!)).thenReturn(true)
        `when`(usuarioRepository.existsByEmailOrCpf(usuarioRecebedor.id!!, usuarioRecebedor.id!!)).thenReturn(true)
        `when`(usuarioRepository.findById(usuarioFazedor.id!!)).thenReturn(Optional.of(usuarioFazedor))
        `when`(usuarioRepository.findByEmailOrCpf(usuarioRecebedor.id!!, usuarioRecebedor.id!!)).thenReturn(
            mutableListOf(usuarioRecebedor))

        pedidoPixListener.processarPix(event)

        assertEquals(BigDecimal(90), usuarioFazedor.saldoContaCorrente)
        assertEquals(BigDecimal(110), usuarioRecebedor.saldoContaCorrente)

        verify(logger, times(1)).info("Pedido de pix do usuário ${event.idUsuario} para a chave pix ${event.chavePix} recebido!")
        verify(logger, times(1)).info("Pix do usuário ${event.idUsuario} para a chave pix ${event.chavePix} feito com sucesso!")
    }

    @Test
    fun `Verificar se a mensagem de resposta e enviada no caso de sucesso`(){
        `when`(usuarioRepository.existsById(usuarioFazedor.id!!)).thenReturn(true)
        `when`(usuarioRepository.existsByEmailOrCpf(usuarioRecebedor.id!!, usuarioRecebedor.id!!)).thenReturn(true)
        `when`(usuarioRepository.findById(usuarioFazedor.id!!)).thenReturn(Optional.of(usuarioFazedor))
        `when`(usuarioRepository.findByEmailOrCpf(usuarioRecebedor.id!!, usuarioRecebedor.id!!)).thenReturn(
            mutableListOf(usuarioRecebedor))

        pedidoPixListener.processarPix(event)

        verify(logger, times(1)).info("Pedido de pix do usuário ${event.idUsuario} para a chave pix ${event.chavePix} recebido!")
        verify(logger, times(1)).info("Mensagem de pix aceito enviada!")
        verify(respostaPixKafkaTemplate, times(1)).send(eq("resposta-pix-topic"),
            eq(event.idTransacao), any())
    }
}