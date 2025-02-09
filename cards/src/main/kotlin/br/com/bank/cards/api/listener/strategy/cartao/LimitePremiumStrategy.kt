package br.com.bank.cards.api.listener.strategy.cartao

import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.utils.enums.Segmento
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class LimitePremiumStrategy: LimiteStrategy {
    override fun definirLimite(cartao: Cartao) {
        if (cartao.segmento.equals(Segmento.PLUS)){
            val limiteAleatorio = (6000..17000 step 100).toList().random().toDouble()

            cartao.limite = BigDecimal.valueOf(limiteAleatorio)
        }
    }
}