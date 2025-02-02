package br.com.bank.cards.api.listener.strategy

import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.utils.enums.Segmento
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class LimitePlusStrategy: LimiteStrategy {
    override fun definirLimite(cartao: Cartao) {
        if (cartao.segmento.equals(Segmento.PLUS)){
            val limiteAleatorio = (4000..6000 step 100).toList().random().toDouble()

            cartao.limite = BigDecimal.valueOf(limiteAleatorio)
        }
    }
}