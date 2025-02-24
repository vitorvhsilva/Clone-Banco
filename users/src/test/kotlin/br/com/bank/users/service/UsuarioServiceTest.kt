package br.com.bank.users.service

import br.com.bank.users.api.dto.events.EnderecoViaCep
import br.com.bank.users.api.dto.events.PagarFaturaEventDTO
import br.com.bank.users.api.dto.events.PedidoCartaoCompletoDTO
import br.com.bank.users.api.dto.input.AtualizarUsuarioDTO
import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.input.PagarFaturaInputDTO
import br.com.bank.users.api.dto.input.PedidoCartaoInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.http.ViaCepClient
import br.com.bank.users.domain.entity.Endereco
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.service.UsuarioService
import br.com.bank.users.domain.service.strategy.SegmentoStrategy
import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusTransacao
import br.com.bank.users.domain.utils.enums.StatusUsuario
import br.com.bank.users.domain.utils.mappers.EnderecoMapper
import br.com.bank.users.domain.utils.mappers.UsuarioMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class UsuarioServiceTest {

    @Mock
    private lateinit var usuarioRepository: UsuarioRepository

    @Mock
    private lateinit var usuarioMapper: UsuarioMapper

    @Mock
    private lateinit var enderecoMapper: EnderecoMapper

    @Mock
    private lateinit var viaCepClient: ViaCepClient

    @Mock
    private lateinit var segmentoStrategy: SegmentoStrategy

    @Mock
    private lateinit var pedidoCartaoKafkaTemplate: KafkaTemplate<String, PedidoCartaoCompletoDTO>

    @Mock
    private lateinit var pagarFaturaKafkaTemplate: KafkaTemplate<String, PagarFaturaEventDTO>

    @Mock
    private lateinit var logger: Logger

    private lateinit var usuarioService: UsuarioService

    private lateinit var usuarioId: String
    private lateinit var usuario: Usuario
    private lateinit var obterUsuarioDTO: ObterUsuarioDTO
    private lateinit var enderecoViaCep: EnderecoViaCep
    private lateinit var endereco: Endereco
    private lateinit var cadastroInputDTO: CadastroUsuarioInputDTO
    private lateinit var cadastroOutputDTO: CadastroUsuarioOutputDTO
    private lateinit var atualizarUsuarioDTO: AtualizarUsuarioDTO

    @BeforeEach
    fun setUp() {
        this.usuarioId = UUID.randomUUID().toString()

        usuarioService = UsuarioService(
            usuarioRepository,
            usuarioMapper,
            enderecoMapper,
            viaCepClient,
            listOf(segmentoStrategy),
            pedidoCartaoKafkaTemplate,
            pagarFaturaKafkaTemplate,
            logger
        )

        this.usuario = Usuario(
            id = usuarioId,
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

        this.cadastroInputDTO = CadastroUsuarioInputDTO(
            nome = "Vitor Hugo",
            email = "vitor@email.com",
            senha = "12345678",
            cpf = "12345678912",
            genero = Genero.MASCULINO,
            saldoContaCorrente = BigDecimal(20000),
            rendaMensal = BigDecimal(5000),
            dataNascimento = LocalDateTime.parse("2006-03-28T15:30:00"),
            cep = "00000000"
        )

        this.cadastroOutputDTO = CadastroUsuarioOutputDTO(
            id = usuarioId,
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

        obterUsuarioDTO = ObterUsuarioDTO (
            id = usuarioId,
            nome = "Vitor Hugo",
            email = "vitor@email.com",
            senha = "12345678",
            cpf = "12345678912",
            genero = Genero.MASCULINO,
            saldoContaCorrente = BigDecimal(20000),
            dataNascimento = LocalDateTime.parse("2006-03-28T15:30:00"),
            agencia = "001",
            segmento = Segmento.PLUS,
            conta = "12345-6",
        )

        this.atualizarUsuarioDTO = AtualizarUsuarioDTO (
            id = usuarioId,
            nome = "Vitor Hugo da Silva",
            senha = "12345678",
            genero = Genero.MASCULINO,
            rendaMensal = BigDecimal(5000)
        )

        this.enderecoViaCep = EnderecoViaCep(
            cep = "",
            logradouro = "",
            complemento = "",
            unidade = "",
            bairro = "",
            localidade = "",
            uf = "",
            estado = "",
            regiao = "",
            ibge = "" ,
            gia = "",
            ddd = "",
            siafi= ""
        )

        this.endereco = Endereco (
            cep = "",
            logradouro = "",
            bairro = "",
            uf = "",
            estado = "",
            regiao = "",
        )

        reset(usuarioRepository, usuarioMapper, enderecoMapper, viaCepClient)
    }

    @Test
    fun `Testar o cadastro de usuario`() {
        `when`( usuarioMapper.cadastroInputParaEntidade(cadastroInputDTO)).thenReturn(usuario)
        `when`( viaCepClient.obterEnderecoDoUsuario("00000000")).thenReturn(enderecoViaCep)
        `when`( enderecoMapper.enderecoParaEntidade(enderecoViaCep)).thenReturn(endereco)
        `when`( usuarioRepository.existsByContaAndAgencia(anyString(), anyString())).thenReturn(false)
        `when`( usuarioRepository.save(usuario)).thenReturn(usuario)
        `when`( usuarioMapper.entidadeParaCadastroOutput(usuario)).thenReturn(cadastroOutputDTO)

        val result = usuarioService.cadastrarUsuario(cadastroInputDTO)

        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertEquals(cadastroOutputDTO, result.body)
        verify(segmentoStrategy, atLeastOnce()).injetarSegmento(usuario)
    }

    @Test
    fun `obterTodosOsUsuarios deve retornar pagina de DTOs`() {
        val page = PageImpl(listOf(usuario))
        `when`( usuarioRepository.findAll(any<Pageable>())).thenReturn(page)
        `when`( usuarioMapper.entidadeParaObterUsuario(usuario)).thenReturn(obterUsuarioDTO)

        val result = usuarioService.obterTodosOsUsuarios(Pageable.unpaged())

        assertEquals(1, result.totalElements)
    }

    @Test
    fun `obterUsuarioPorId deve lancar excecao quando nao encontrado`() {
        `when` ( usuarioRepository.findById(usuarioId) ).thenReturn(Optional.empty())

        assertThrows<NotFoundException> {
            usuarioService.obterUsuarioPorId(usuarioId)
        }
    }

    @Test
    fun `atualizarUsuario deve atualizar campos corretamente`() {
        val dto = atualizarUsuarioDTO
        `when` ( usuarioRepository.findById(usuarioId) ).thenReturn(Optional.of(usuario))
        `when` ( usuarioMapper.entidadeParaObterUsuarioDetalhado(usuario) ).thenReturn(ObterUsuarioDetalhadoDTO(
            id = usuarioId,
            email = "vitor@email.com",
            senha = "12345678",
            cpf = "12345678912",
            genero = Genero.MASCULINO,
            saldoContaCorrente = BigDecimal(20000),
            dataNascimento = LocalDateTime.parse("2006-03-28T15:30:00"),
            statusUsuario = StatusUsuario.ATIVO,
            dataCriacaoConta = LocalDateTime.now(),
            agencia = "001",
            segmento = Segmento.PLUS,
            conta = "12345-6",

            nome = "Vitor Hugo da Silva",
            rendaMensal = BigDecimal(10000)
        ))

        val result = usuarioService.atualizarUsuario(dto)

        assertEquals("Vitor Hugo da Silva", usuario.nome)
        assertNotNull(result)
    }

    @Test
    fun `usuarioReceberSalario deve adicionar renda mensal ao saldo`() {
        usuario.rendaMensal = BigDecimal.valueOf(1000)
        usuario.saldoContaCorrente = BigDecimal.ZERO
        `when` ( usuarioRepository.findById(usuarioId) ).thenReturn(Optional.of(usuario))
        `when` ( usuarioMapper.entidadeParaObterUsuario(usuario) ).thenReturn(obterUsuarioDTO)

        val result = usuarioService.usuarioReceberSalario(usuarioId)

        assertEquals(BigDecimal.valueOf(1000), usuario.saldoContaCorrente)
        assertNotNull(result)
    }

    @Test
    fun `fazerPedidoDeCartao deve enviar mensagem Kafka`() {
        val dto = PedidoCartaoInputDTO(idUsuario = usuarioId, idCartao = 3)
        `when` (usuarioRepository.findById(usuarioId) ).thenReturn(Optional.of(usuario))

        val result = usuarioService.fazerPedidoDeCartao(dto)

        assertEquals(HttpStatus.CREATED, result.statusCode)
        verify(pedidoCartaoKafkaTemplate, times(1)).send(eq("pedido-cartoes-topic"), eq(usuarioId), any())
    }

    @Test
    fun `pagarFaturaDoCartao deve diminuir saldo e enviar mensagem`() {
        val dto = PagarFaturaInputDTO(
            idUsuario = usuarioId,
            valorFatura = BigDecimal.valueOf(500),
            idCartao = "",
            mesAnoFatura = ""
        )
        usuario.saldoContaCorrente = BigDecimal.valueOf(1000)
        `when` ( usuarioRepository.findById(usuarioId) ).thenReturn(Optional.of(usuario))

        val result = usuarioService.pagarFaturaDoCartao(dto)

        assertEquals(BigDecimal.valueOf(500), usuario.saldoContaCorrente)
        assertEquals(StatusTransacao.EM_PROCESSAMENTO, result.status)
        verify(pagarFaturaKafkaTemplate, times(1)).send(eq("pagar-fatura-topic"), eq(usuarioId), any())
    }
}