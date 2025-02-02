package br.com.bank.cards.domain.utils.mappers

import br.com.bank.cards.api.dto.output.CartaoOutputDTO
import br.com.bank.cards.domain.entity.Cartao
import br.com.bank.cards.domain.utils.enums.Bandeira
import br.com.bank.cards.domain.utils.enums.Segmento
import br.com.bank.cards.domain.utils.enums.TipoCartao
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

@Component
class CartaoMapperImpl: CartaoMapper {
    override fun entidadeParaOutput(cartao: Cartao): CartaoOutputDTO {
        return CartaoOutputDTO(
            idCartao = cartao.idCartao,
            idUsuario = cartao.idUsuario,
            nomeCartao = cartao.nomeCartao,
            nomeUsuario = cartao.nomeUsuario,
            numeroCartao = cartao.numeroCartao,
            codigoSeguranca = cartao.codigoSeguranca,
            agencia = cartao.agencia,
            conta = cartao.conta,
            limite = cartao.limite,
            bandeira = cartao.bandeira,
            segmento = cartao.segmento,
            tipoCartao = cartao.tipoCartao
        )
    }
}