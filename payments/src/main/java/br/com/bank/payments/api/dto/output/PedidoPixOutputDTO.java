package br.com.bank.payments.api.dto.output;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoPixOutputDTO {
    private String idTransacao;
    private String idUsuario;
    private BigDecimal valor;
    private StatusTransacao status;
    private String chavePix;
}
