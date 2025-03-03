package br.com.bank.cards.listener

import br.com.bank.cards.api.dto.events.PagarFaturaEventDTO
import br.com.bank.cards.api.listener.PagarFaturaListener
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.entity.Fatura
import br.com.bank.cards.domain.repository.CartaoRepository
import br.com.bank.cards.domain.repository.FaturaRepository
import br.com.bank.cards.domain.utils.enums.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class PagarFaturaListenerTest {
    @Mock
    private lateinit var logger: Logger
    @Mock
    private lateinit var faturaRepository: FaturaRepository
    @Mock
    private lateinit var cartaoRepository: CartaoRepository
    @Mock
    private lateinit var respostaFaturaKafkaTemplate: KafkaTemplate<String, PagarFaturaEventDTO>
    @InjectMocks
    private lateinit var pagarFaturaListener: PagarFaturaListener

    private lateinit var event: PagarFaturaEventDTO
    private lateinit var fatura: Fatura
    private lateinit var cartao: Cartao

    @BeforeEach
    fun setUp() {
        event = PagarFaturaEventDTO(
            idUsuario = "idUsuarioTeste",
            idCartao = "idCartaoTeste",
            valorFatura = BigDecimal(10),
            mesAnoFatura = "03/28",
            status = StatusTransacao.EM_PROCESSAMENTO,
            mensagem = ""
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

        fatura = Fatura(
            idFatura = "",
            cartao = cartao,
            valorFatura = BigDecimal(10),
            mesAnoFatura = "03/28",
            status = StatusFatura.ABERTA
        )
    }

    @Test
    fun `Retornar mensagem de erro se a fatura nao existir`() {
        `when`(faturaRepository.findByMesAnoFatura(event.mesAnoFatura)).thenReturn(Optional.empty())

        pagarFaturaListener.processarPedido(event)

        verify(logger, times(1)).error("A fatura desse mês não existe!")
        assertEquals(event.status, StatusTransacao.INVALIDA)
        assertEquals(event.mensagem, "A fatura desse mês não existe!")

        verify(respostaFaturaKafkaTemplate, times(1))
            .send(eq("resposta-fatura-topic"), eq(event.idUsuario), any())
    }

    @Test
    fun `Retornar mensagem de erro se o valor da fatura for diferente do que deveria`() {
        fatura.valorFatura = BigDecimal(0)
        `when`(faturaRepository.findByMesAnoFatura(event.mesAnoFatura)).thenReturn(Optional.of(fatura))

        pagarFaturaListener.processarPedido(event)

        verify(logger, times(1)).error("Valor da fatura diferente do que deveria!")
        assertEquals(event.status, StatusTransacao.INVALIDA)
        assertEquals(event.mensagem, "Valor da fatura diferente do que deveria!")

        verify(respostaFaturaKafkaTemplate, times(1))
            .send(eq("resposta-fatura-topic"), eq(event.idUsuario), any())
    }

    @Test
    fun `Retornar mensagem de erro se a fatura ja foi paga`() {
        fatura.status = StatusFatura.PAGA
        `when`(faturaRepository.findByMesAnoFatura(event.mesAnoFatura)).thenReturn(Optional.of(fatura))

        pagarFaturaListener.processarPedido(event)

        verify(logger, times(1)).error("Fatura já foi paga!")
        assertEquals(event.status, StatusTransacao.INVALIDA)
        assertEquals(event.mensagem, "Fatura já foi paga!")

        verify(respostaFaturaKafkaTemplate, times(1))
            .send(eq("resposta-fatura-topic"), eq(event.idUsuario), any())
    }

    @Test
    fun `Fatura atualizada e mensagem kafka enviada em caso de sucesso`() {
        `when`(faturaRepository.findByMesAnoFatura(event.mesAnoFatura)).thenReturn(Optional.of(fatura))
        `when`(cartaoRepository.findById(event.idCartao)).thenReturn(Optional.of(cartao))

        pagarFaturaListener.processarPedido(event)

        assertEquals(event.status, StatusTransacao.VALIDA)
        assertEquals(fatura.status, StatusFatura.PAGA)
        assertEquals(event.mensagem, "Fatura processada com sucesso!")

        verify(logger, times(1))
            .info("Pedido de pagar fatura do usuário ${event.idUsuario} para o mês ${event.mesAnoFatura} processado!")
        verify(respostaFaturaKafkaTemplate, times(1))
            .send(eq("resposta-fatura-topic"), eq(event.idUsuario), any())
    }

}