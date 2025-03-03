package br.com.bank.cards.strategy

import br.com.bank.cards.api.listener.strategy.cartao.*
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.enums.TipoCartao
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class LimiteStrategyTest {

    private val strategys: List<LimiteStrategy> = listOf(LimiteBaseStrategy(), LimiteInfinityStrategy(),
        LimitePlusStrategy(), LimitePremiumStrategy())

    private lateinit var cartao: Cartao

    @BeforeEach
    fun setUp() {
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
    }

    @Test
    fun `Testar injecao de limite para o segmento Base`(){
        strategys.forEach{
            s -> s.definirLimite(cartao)
        }

        val valido = cartao.limite >= BigDecimal(2000) ||  cartao.limite <= BigDecimal(3000)

        assertEquals(true, valido)
    }

    @Test
    fun `Testar injecao de limite para o segmento Infinity`(){
        strategys.forEach{
                s -> s.definirLimite(cartao)
        }

        val valido = cartao.limite >= BigDecimal(20000) ||  cartao.limite <= BigDecimal(50000)

        assertEquals(true, valido)
    }

    @Test
    fun `Testar injecao de limite para o segmento Plus`(){
        strategys.forEach{
                s -> s.definirLimite(cartao)
        }

        val valido = cartao.limite >= BigDecimal(4000) ||  cartao.limite <= BigDecimal(6000)

        assertEquals(true, valido)
    }

    @Test
    fun `Testar injecao de limite para o segmento Premium`(){
        strategys.forEach{
                s -> s.definirLimite(cartao)
        }

        val valido = cartao.limite >= BigDecimal(6000) ||  cartao.limite <= BigDecimal(17000)

        assertEquals(true, valido)
    }
}