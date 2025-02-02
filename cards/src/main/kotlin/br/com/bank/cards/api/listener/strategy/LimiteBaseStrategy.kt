package br.com.bank.cards.api.listener.strategy

import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.utils.enums.Segmento
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class LimiteBaseStrategy: LimiteStrategy {
    override fun definirLimite(cartao: Cartao) {
        if (cartao.segmento.equals(Segmento.BASE)){
            val limiteAleatorio = (2000..3000 step 100).toList().random().toDouble()

            cartao.limite = BigDecimal.valueOf(limiteAleatorio)
        }
    }
}