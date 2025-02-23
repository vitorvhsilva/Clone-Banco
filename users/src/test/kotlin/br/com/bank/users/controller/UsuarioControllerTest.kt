package br.com.bank.users.controller

import br.com.bank.users.api.dto.events.EnderecoViaCep
import br.com.bank.users.api.dto.events.PagarFaturaEventDTO
import br.com.bank.users.api.dto.input.AtualizarUsuarioDTO
import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.input.PagarFaturaInputDTO
import br.com.bank.users.api.dto.input.PedidoCartaoInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.http.ViaCepClient
import br.com.bank.users.domain.entity.Endereco
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.service.UsuarioService
import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.Segmento
import br.com.bank.users.domain.utils.enums.StatusTransacao
import br.com.bank.users.domain.utils.enums.StatusUsuario
import br.com.bank.users.domain.utils.mappers.UsuarioMapperImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var usuarioRepository: UsuarioRepository

    @MockitoBean
    private lateinit var usuarioService: UsuarioService

    @MockitoBean
    private lateinit var usuarioMapperImpl: UsuarioMapperImpl

    @MockitoBean
    private lateinit var viaCepClient: ViaCepClient

    private fun criarUsuario(id: String): Usuario {
        return Usuario(
            id = id,
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
    }

    private fun criarUsuarioDetalhado(usuario: Usuario) : ObterUsuarioDetalhadoDTO {
        return ObterUsuarioDetalhadoDTO(
            id = usuario.id!!,
            nome = usuario.nome,
            email = usuario.email,
            cpf = usuario.cpf,
            genero = usuario.genero,
            dataNascimento = usuario.dataNascimento,
            statusUsuario = usuario.statusUsuario,
            agencia = usuario.agencia,
            conta = usuario.conta,
            dataCriacaoConta = usuario.dataCriacaoConta,
            segmento = usuario.segmento,
            saldoContaCorrente = usuario.saldoContaCorrente,
            rendaMensal = usuario.rendaMensal,
            senha = usuario.senha
        )
    }

    @Test
    @Throws(Exception::class)
    fun `Deve retornar codigo 200 para listar os usuarios`() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/usuarios")
        ).andReturn().response

        Assertions.assertEquals(200, response.status)
    }

    @Test
    @Throws(Exception::class)
    fun `Deve retornar codigo 200 para listar o usuario por id`() {
        val id = UUID.randomUUID().toString()
        val usuario = criarUsuario(id)
        val usuarioDetalhadoDTO = criarUsuarioDetalhado(usuario)

        `when`(usuarioRepository.findById(usuario.id!!)).thenReturn(Optional.of(usuario))

        `when`(usuarioMapperImpl.entidadeParaObterUsuarioDetalhado(usuario)).thenReturn(usuarioDetalhadoDTO)

        `when`(usuarioService.obterUsuarioPorId(usuario.id!!)).thenReturn(usuarioDetalhadoDTO)

        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/usuarios/${usuario.id}")
        ).andReturn().response

        Assertions.assertEquals(200, response.status)
    }

    @Test
    @Throws(Exception::class)
    fun `Deve retornar codigo 400 para quando o usuario nao existe por id`() {
        val id = UUID.randomUUID().toString()

        `when`(usuarioService.obterUsuarioPorId(id))
            .thenThrow(NotFoundException("Usuário não encontrado!"))

        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/usuarios/$id")
        ).andReturn().response

        Assertions.assertEquals(400, response.status)
    }

    @Test
    @Throws(Exception::class)
    fun `Deve retornar codigo 201 para cadastrar usuario`() {
        val id = UUID.randomUUID().toString()

        val json = """
        {
            "nome": "Vitor Hugo",
            "email": "vitor@email.com",
            "senha": "12345678",
            "cpf": "12345678912",
            "genero": "MASCULINO",
            "saldoContaCorrente": 20000,
            "rendaMensal": 5000,
            "dataNascimento": "2006-03-28T15:30:00",
            "cep": "01001000"
        }
        """.trimIndent()

        val cadastroUsuarioInputDTO = CadastroUsuarioInputDTO(
            nome = "Vitor Hugo",
            email= "vitor@email.com",
            senha= "12345678",
            cpf= "12345678912",
            genero= Genero.MASCULINO,
            saldoContaCorrente = BigDecimal(20000),
            rendaMensal = BigDecimal(5000),
            dataNascimento = LocalDateTime.parse("2006-03-28T15:30:00"),
            cep = "01001000"
        )

        val usuarioSalvo = criarUsuario(id)

        val enderecoViaCep = EnderecoViaCep(
            cep = "",
            logradouro = "",
            complemento = "",
            bairro = "",
            localidade = "",
            uf = "",
            unidade = "",
            ddd = "",
            estado = "",
            gia = "",
            ibge = "",
            regiao = "",
            siafi = ""
        )

        val usuarioOutputDTO = CadastroUsuarioOutputDTO(
            id = usuarioSalvo.id!!,
            nome = usuarioSalvo.nome,
            email = usuarioSalvo.email,
            cpf = usuarioSalvo.cpf,
            genero = usuarioSalvo.genero,
            saldoContaCorrente = usuarioSalvo.saldoContaCorrente,
            rendaMensal = usuarioSalvo.rendaMensal,
            dataNascimento = usuarioSalvo.dataNascimento,
            agencia = usuarioSalvo.agencia,
            endereco = usuarioSalvo.endereco,
            conta = usuarioSalvo.conta,
            segmento = usuarioSalvo.segmento,
            dataCriacaoConta = usuarioSalvo.dataCriacaoConta,
            senha = usuarioSalvo.senha,
            statusUsuario = usuarioSalvo.statusUsuario
        )

        `when`(usuarioRepository.save(eq(usuarioSalvo))).thenReturn(usuarioSalvo)

        `when`(viaCepClient.obterEnderecoDoUsuario("01001000")).thenReturn(enderecoViaCep)

        `when`(usuarioRepository.existsByContaAndAgencia(anyString(), anyString())).thenReturn(false)

        `when`(usuarioService.cadastrarUsuario(cadastroUsuarioInputDTO))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(usuarioOutputDTO))

        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/usuarios")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        Assertions.assertEquals(201, response.status)
    }

    @Test
    fun `Deve retornar 200 quando atualizar o usuario`() {

        val id = UUID.randomUUID().toString()

        val json = """
        {
            "id": "${id}",
            "nome": "Vitor Hugo",
            "senha": "12345678",
            "genero": "MASCULINO",
            "rendaMensal": 6000
        }
        """.trimIndent()

        val atualizarUsuario = AtualizarUsuarioDTO (
            id = id,
            nome = "Vitor Hugo",
            senha = "12345678",
            genero = Genero.MASCULINO,
            rendaMensal = BigDecimal(6000),
        )

        val usuarioSalvo = criarUsuario(id)

        val usuarioDetalhado = criarUsuarioDetalhado(usuarioSalvo)

        `when`(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioSalvo))

        `when`(usuarioMapperImpl.entidadeParaObterUsuarioDetalhado(usuarioSalvo)).thenReturn(usuarioDetalhado)

        `when`(usuarioService.atualizarUsuario(atualizarUsuario)).thenReturn(usuarioDetalhado)

        val response = mockMvc.perform(
            MockMvcRequestBuilders.put("/usuarios")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        Assertions.assertEquals(200, response.status)

    }

    @Test
    fun `Deve retornar 200 quando o usuario receber salario`() {
        val id = UUID.randomUUID().toString()

        val usuarioSalvo = criarUsuario(id)

        `when`(usuarioRepository.findById(usuarioSalvo.id!!)).thenReturn(Optional.of(usuarioSalvo))

        val response = mockMvc.perform(
            MockMvcRequestBuilders.put("/usuarios/1")
        ).andReturn().response

        Assertions.assertEquals(200, response.status)
    }

    @Test
    fun `Deve retornar 201 quando o usuario pedir um cartao`() {

        val id = UUID.randomUUID().toString()

        val json = """
        {
            "idUsuario": "${id}",
            "idCartao": 3
        }
        """.trimIndent()

        val dto = PedidoCartaoInputDTO (
            idUsuario= id,
            idCartao = 3
        )

        val usuarioSalvo = criarUsuario(id)

        `when`(usuarioRepository.findById(usuarioSalvo.id!!)).thenReturn(Optional.of(usuarioSalvo))

        `when`(usuarioService.fazerPedidoDeCartao(dto)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).build())

        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/usuarios/cartoes")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        Assertions.assertEquals(201, response.status)
    }

    @Test
    fun `Deve retornar 200 quando o usuario pedir para pagar uma fatura`() {

        val id = UUID.randomUUID().toString()

        val json = """
        {
            "idCartao": "00e82431-3f15-4f7d-9645-94ab79e5ba28",
            "idUsuario": "0d44dd96-cbed-4cd1-89bc-10828747676a",
            "valorFatura": 100.00,
            "mesAnoFatura": "02/2025"
        }
        """.trimIndent()

        val dto = PagarFaturaInputDTO (
            idCartao = "00e82431-3f15-4f7d-9645-94ab79e5ba28",
            idUsuario = "0d44dd96-cbed-4cd1-89bc-10828747676a",
            valorFatura = BigDecimal(100.00),
            mesAnoFatura = "02/2025"
        )

        val pagarFatura = PagarFaturaEventDTO(
            idUsuario = dto.idUsuario,
            idCartao = dto.idCartao,
            valorFatura = dto.valorFatura,
            mesAnoFatura = dto.mesAnoFatura,
            status = StatusTransacao.EM_PROCESSAMENTO,
            mensagem = ""
        )

        val usuarioSalvo = criarUsuario(id)

        `when`(usuarioRepository.findById(usuarioSalvo.id!!)).thenReturn(Optional.of(usuarioSalvo))

        `when`(usuarioService.pagarFaturaDoCartao(dto)).thenReturn(pagarFatura)

        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/usuarios/faturas")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        Assertions.assertEquals(200, response.status)
    }
}