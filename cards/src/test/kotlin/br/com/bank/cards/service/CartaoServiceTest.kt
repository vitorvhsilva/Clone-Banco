package br.com.bank.cards.service

import br.com.bank.cards.api.dto.output.CartaoOutputDTO
import br.com.bank.cards.api.dto.output.CatalogoCartaoOutputDTO
import br.com.bank.cards.api.dto.output.FaturaOutputDTO
import br.com.bank.cards.api.listener.strategy.cartao.*
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.CatalogoCartoes
import br.com.bank.cards.domain.entity.Fatura
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.cards.domain.service.CartaoService
import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.enums.StatusFatura
import br.com.bank.cards.domain.utils.enums.TipoCartao
import br.com.bank.cards.domain.utils.mappers.CartaoMapper
import br.com.bank.cards.domain.utils.mappers.CatalogoCartaoMapper
import br.com.bank.cards.domain.utils.mappers.FaturaMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class CartaoServiceTest {

    @Mock
    private lateinit var catalogoMapper: CatalogoCartaoMapper
    @Mock
    private lateinit var cartaoMapper: CartaoMapper
    @Mock
    private lateinit var faturaMapper: FaturaMapper
    @Mock
    private lateinit var catalogoRepository: CatalogoCartoesRepository
    @Mock
    private lateinit var cartaoRepository: CartaoRepository
    @Mock
    private lateinit var faturaRepository: FaturaRepository

    private lateinit var strategys: List<LimiteStrategy>

    private lateinit var cartaoService: CartaoService

    private lateinit var cartao: Cartao
    private lateinit var cartaoOutput: CartaoOutputDTO

    private lateinit var fatura: Fatura
    private lateinit var faturaOutput: FaturaOutputDTO


    @BeforeEach
    fun setUp() {

        strategys = listOf(
            LimiteBaseStrategy(),
            LimiteInfinityStrategy(),
            LimitePlusStrategy(),
            LimitePremiumStrategy()
        )

        cartaoService = CartaoService(
            catalogoMapper,
            cartaoMapper,
            faturaMapper,
            catalogoRepository,
            cartaoRepository,
            faturaRepository,
            strategys
        )

        cartao = Cartao(
            idCartao = "idCartaoTeste",
            idUsuario = "idUsuarioTeste",
            idCatalogo = 2,
            nomeCartao = "Conquista",
            nomeUsuario = "Vitor",
            numeroCartao = "1234567812345678",
            codigoSeguranca = "123",
            agencia = "123",
            conta = "123",
            limite = BigDecimal(0),
            segmento = Segmento.BASE,
            bandeira = Bandeira.MASTERCARD,
            tipoCartao = TipoCartao.CREDITO,
            faturas = emptyList()
        )

        cartaoOutput = CartaoOutputDTO (
            idCartao = "idCartaoTeste",
            idUsuario = "idUsuarioTeste",
            nomeCartao = "Conquista",
            nomeUsuario = "Vitor",
            numeroCartao = "1234567812345678",
            codigoSeguranca = "123",
            agencia = "123",
            conta = "123",
            limite = BigDecimal(0),
            segmento = Segmento.BASE,
            bandeira = Bandeira.MASTERCARD,
            tipoCartao = TipoCartao.CREDITO
        )

        fatura = Fatura(
            idFatura = "idFaturaTeste",
            cartao = cartao,
            valorFatura = BigDecimal(10),
            mesAnoFatura = "03/28",
            status = StatusFatura.ABERTA
        )

        faturaOutput = FaturaOutputDTO(
            idFatura = "idFaturaTeste",
            valorFatura = BigDecimal(10),
            mesAnoFatura = "03/28",
            status = StatusFatura.ABERTA
        )
    }

    @Test
    fun `Obter cartoes por segmento deve retornar lista de cartoes`() {
        val segmento = Segmento.BASE
        val catalogoCartoes = listOf(
            CatalogoCartoes(1, "Essencial", Bandeira.VISA, segmento),
            CatalogoCartoes(2, "Conquista", Bandeira.MASTERCARD, segmento)
        )

        val outputDTOs = listOf(
            CatalogoCartaoOutputDTO(1, "Essencial", Bandeira.VISA, segmento),
            CatalogoCartaoOutputDTO(2, "Conquista", Bandeira.MASTERCARD, segmento)
        )

        `when`(catalogoRepository.findAllBySegmento(segmento)).thenReturn(catalogoCartoes)
        `when`(catalogoMapper.entidadeParaOutput(catalogoCartoes[0])).thenReturn(outputDTOs[0])
        `when`(catalogoMapper.entidadeParaOutput(catalogoCartoes[1])).thenReturn(outputDTOs[1])

        val result = cartaoService.obterCartoesDisponiveisParaSegmento(segmento)

        assertEquals(2, result.size)
        assertEquals("Essencial", result[0].nome)
        assertEquals("Conquista", result[1].nome)
    }

    @Test
    fun `Obter cartoes por segmento deve lancar excecao quando lista estiver vazia`() {
        val segmento = Segmento.PREMIUM
        `when`(catalogoRepository.findAllBySegmento(segmento)).thenReturn(emptyList())

        val exception = assertThrows<RuntimeException> {
            cartaoService.obterCartoesDisponiveisParaSegmento(segmento)
        }

        assertEquals("Lista de cartões vazia!", exception.message)
    }

    @Test
    fun `Obter cartoes do usuario deve retornar lista de cartoes`() {
        val id = "1"
        val cartoes = mutableListOf(cartao)
        `when`(cartaoRepository.findAllByIdUsuario(id)).thenReturn(mutableListOf(cartao))
        `when`(cartaoMapper.entidadeParaOutput(cartoes[0])).thenReturn(cartaoOutput)

        val cartoesPegos: MutableList<CartaoOutputDTO> = mutableListOf(cartaoOutput)
        val result = cartaoService.obterCartoesDisponiveisDoUsuario(id)

        assertEquals(cartoesPegos.size, result.size)
    }

    @Test
    fun `Obter faturas do cartao deve retornar lista de faturas`() {
        val id = "1"
        val faturas = mutableListOf(fatura)
        `when`(faturaRepository.findAllByCartao_IdCartao(id)).thenReturn(faturas)
        `when`(faturaMapper.entidadeParaOutput(faturas[0])).thenReturn(faturaOutput)

        val faturasPegas: MutableList<FaturaOutputDTO> = mutableListOf(faturaOutput)
        val result = cartaoService.obterFaturaDoCartao(id)

        assertEquals(faturasPegas.size, result.size)
    }

    @Test
    fun `Verificar a injecao do numero do cartao e codigo de seguranca`() {
        cartaoService.injetarNumeroECodSeguranca(cartao)

        assertEquals(cartao.numeroCartao.length, 16)
        assertEquals(cartao.codigoSeguranca.length,3)
    }

    @Test
    fun `Verificar a injecao de limite`() {
        cartaoService.injetarLimite(cartao)

        assertEquals(true, cartao.limite != BigDecimal(0))
    }
}