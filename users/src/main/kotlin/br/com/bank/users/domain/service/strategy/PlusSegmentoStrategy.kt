package br.com.bank.users.domain.service.strategy

import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.utils.enums.Segmento
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PlusSegmentoStrategy: SegmentoStrategy {
    override fun injetarSegmento(usuario: Usuario) {
        if (usuario.rendaMensal > BigDecimal("2000") && usuario.rendaMensal <= BigDecimal("5000")) {
            usuario.segmento = Segmento.PLUS
        }
    }
}