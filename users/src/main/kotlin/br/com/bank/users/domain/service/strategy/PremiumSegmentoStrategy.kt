package br.com.bank.users.domain.service.strategy

import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.utils.enums.Segmento
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PremiumSegmentoStrategy: SegmentoStrategy {
    override fun injetarSegmento(usuario: Usuario) {
        if (usuario.rendaMensal > BigDecimal("5000") && usuario.rendaMensal <= BigDecimal("15000")) {
            usuario.segmento = Segmento.PREMIUM
        }
    }
}