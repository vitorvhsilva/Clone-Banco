package br.com.bank.cards.listener

import br.com.bank.cards.api.dto.events.PedidoCartaoCompletoEventDTO
import br.com.bank.cards.api.listener.PedidoCartaoListener
import br.com.bank.cards.api.listener.strategy.cartao.*
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.CatalogoCartoes
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.CatalogoCartoesRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.cards.domain.service.CartaoService
import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.enums.TipoCartao
import br.com.bank.cards.domain.utils.mappers.CartaoMapper
import br.com.bank.cards.domain.utils.mappers.CatalogoCartaoMapper
import br.com.bank.cards.domain.utils.mappers.FaturaMapper
import br.com.bank.users.api.exception.CardAlreadyMadeException
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.exception.SegmentoNotAllowedException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class PedidoCartaoListenerTest {

    @Mock
    private lateinit var logger: Logger
    @Mock
    private lateinit var cartaoRepository: CartaoRepository
    @Mock
    private lateinit var catalogoRepository: CatalogoCartoesRepository
    @Mock
    private lateinit var catalogoMapper: CatalogoCartaoMapper
    @Mock
    private lateinit var cartaoMapper: CartaoMapper
    @Mock
    private lateinit var faturaMapper: FaturaMapper
    @Mock
    private lateinit var faturaRepository: FaturaRepository
    @Mock
    private val strategys: List<LimiteStrategy> = listOf(LimiteBaseStrategy(), LimiteInfinityStrategy(),
        LimitePlusStrategy(), LimitePremiumStrategy())

    @Mock
    private lateinit var cartaoService: CartaoService

    @InjectMocks
    private lateinit var pedidoCartaoListener: PedidoCartaoListener

    private lateinit var event: PedidoCartaoCompletoEventDTO
    private lateinit var cartao: Cartao
    private lateinit var cartaoCatalogo: CatalogoCartoes
    private lateinit var cartaoCatalogoComSegmentoErrado: CatalogoCartoes

    @BeforeEach
    fun setUp() {
        event = PedidoCartaoCompletoEventDTO(
            idCartao = 2,
            idUsuario = "idUsuarioTeste",
            nomeUsuario = "Vitor",
            agencia = "123",
            conta = "123",
            segmento = Segmento.BASE
        )

        cartao = Cartao(
            idCartao = "idCartaoTeste",
            idUsuario = "idUsuarioTeste",
            idCatalogo = 2,
            nomeCartao = "Conquista",
            nomeUsuario = "Vitor",
            numeroCartao = "",
            codigoSeguranca = "",
            agencia = "123",
            conta = "123",
            limite = BigDecimal(0),
            segmento = Segmento.BASE,
            bandeira = Bandeira.MASTERCARD,
            tipoCartao = TipoCartao.CREDITO,
            faturas = emptyList()
        )

        cartaoCatalogo = CatalogoCartoes(
            id = 2,
            nome = "Conquista",
            bandeira = Bandeira.MASTERCARD,
            segmento = Segmento.BASE
        )

        cartaoCatalogoComSegmentoErrado = CatalogoCartoes(
            id = 2,
            nome = "Teste",
            bandeira = Bandeira.MASTERCARD,
            segmento = Segmento.INFINITY
        )
    }

    @Test
    fun `Pedido de cartao retorna erro se nao encontra ele no catalogo`() {

        `when`(catalogoRepository.findById(event.idCartao)).thenThrow(NotFoundException("Cartão não encontrado!"))

        assertThrows<NotFoundException> {
            pedidoCartaoListener.processarPedido(event)
        }
    }

    @Test
    fun `Pedido de cartao retorna erro se o cartao do catalogo e de segmento diferente`() {

        `when`(catalogoRepository.findById(event.idCartao)).thenReturn(Optional.of(cartaoCatalogoComSegmentoErrado))

        assertThrows<SegmentoNotAllowedException> {
            pedidoCartaoListener.processarPedido(event)
        }
        verify(logger, times(1)).error("Segmento do usuário não permite esse cartão!")
    }

    @Test
    fun `Pedido de cartao deve retornar erro se o cartao ja existir`() {
        `when`(catalogoRepository.findById(event.idCartao)).thenReturn(Optional.of(cartaoCatalogo))
        `when`(cartaoRepository.findAllByIdUsuario(event.idUsuario)).thenReturn(mutableListOf(cartao))

        assertThrows<CardAlreadyMadeException> {
            pedidoCartaoListener.processarPedido(event)
        }
        verify(logger, times(1)).error("Esse cartão já foi feito!")
    }


}