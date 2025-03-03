package br.com.bank.cards.api.listener

import br.com.bank.cards.api.dto.events.PedidoCreditoEventDTO
import br.com.bank.cards.api.dto.events.RespostaCreditoEventDTO
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.Fatura
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.cards.domain.utils.enums.*
import br.com.bank.users.api.exception.NotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ExtendWith(MockitoExtension::class)
class PedidoCreditoListenerTest {

    @Mock
    private lateinit var cartaoRepository: CartaoRepository

    @Mock
    private lateinit var faturaRepository: FaturaRepository

    @Mock
    private lateinit var respostaCreditoKafkaTemplate: KafkaTemplate<String, RespostaCreditoEventDTO>

    @Mock
    private lateinit var logger: Logger

    @InjectMocks
    private lateinit var pedidoCreditoListener: PedidoCreditoListener

    private lateinit var cartao: Cartao
    private lateinit var event: PedidoCreditoEventDTO

    @BeforeEach
    fun setUp() {
        cartao = Cartao(
            idCartao = "idCartaoTeste",
            limite = BigDecimal(1000),

            idUsuario = "idUsuarioTeste",
            idCatalogo = 2,
            nomeCartao = "Conquista",
            nomeUsuario = "Vitor",
            numeroCartao = "1234567812345678",
            codigoSeguranca = "123",
            agencia = "123",
            conta = "123",
            segmento = Segmento.BASE,
            bandeira = Bandeira.MASTERCARD,
            tipoCartao = TipoCartao.CREDITO,
            faturas = emptyList()
        )

        event = PedidoCreditoEventDTO(
            idTransacao = "idTransacaoTeste",
            idUsuario = "idUsuarioTeste",
            idCartao = "idCartaoTeste",
            valor = BigDecimal(600),
            qtdParcelas = 3,
            chaveEstabelecimentoComercial = "idChaveTeste"
        )
    }

    @Test
    fun `Retornar excecao se nao encontrar o cartao`() {
        `when`(cartaoRepository.findById(event.idCartao)).thenReturn(Optional.empty())

        assertThrows<NotFoundException> {
            pedidoCreditoListener.processarPedidoCredito(event)
        }
    }

    @Test
    fun `Pedido de credito deve ser rejeitado se o valor foi maior que o limite`() {
        event = event.copy(valor = BigDecimal(1500))
        `when`(cartaoRepository.findById(event.idCartao)).thenReturn(Optional.of(cartao))

        pedidoCreditoListener.processarPedidoCredito(event)

        verify(respostaCreditoKafkaTemplate, times(1)).send(eq("resposta-credito-topic"), eq(event.idTransacao), any())
        verify(logger, times(1)).error("Valor do crédito maior que o limite!")
    }

    @Test
    fun `Pedido de credito deve ser processado e mandar mensagem em caso de sucesso`() {
        `when`(cartaoRepository.findById(event.idCartao)).thenReturn(Optional.of(cartao))
        `when`(faturaRepository.findByMesAnoFatura(anyString())).thenReturn(Optional.empty())

        pedidoCreditoListener.processarPedidoCredito(event)

        assertEquals(BigDecimal(400), cartao.limite) // 1000 - 600 = 400
        verify(respostaCreditoKafkaTemplate, times(1)).send(eq("resposta-credito-topic"), eq(event.idTransacao), any())
        verify(logger, times(1)).info("Pedido de crédito processado!")
    }

    @Test
    fun `Pedido de credito deve consolidar fatura existente`() {
        val faturaExistente = Fatura(
            idFatura = "idFaturaTeste",
            cartao = cartao,
            valorFatura = BigDecimal(200),
            mesAnoFatura = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")),
            status = StatusFatura.ABERTA
        )
        `when`(cartaoRepository.findById(event.idCartao)).thenReturn(Optional.of(cartao))
        `when`(faturaRepository.findByMesAnoFatura(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"))))
            .thenReturn(Optional.of(faturaExistente))

        pedidoCreditoListener.processarPedidoCredito(event)


        assertEquals(BigDecimal(400), faturaExistente.valorFatura) // 200 (ja existia) + 200 (1 das 3 parcelas) = 400
        assertEquals(BigDecimal(400), cartao.limite) // 1000 - 600 = 400
        verify(logger).info(contains("Fatura consolidada!"))
    }

    @Test
    fun `Pedido de credito deve criar fatura se nao existir nenhuma`() {
        `when`(cartaoRepository.findById(event.idCartao)).thenReturn(Optional.of(cartao))
        `when`(faturaRepository.findByMesAnoFatura(anyString())).thenReturn(Optional.empty())

        pedidoCreditoListener.processarPedidoCredito(event)

        verify(faturaRepository, times(3)).save(any(Fatura::class.java))
        verify(logger, times(3)).info(contains("Fatura criada!"))
    }

}