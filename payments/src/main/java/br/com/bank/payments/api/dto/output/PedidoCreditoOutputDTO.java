package br.com.bank.payments.api.dto.output;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoCreditoOutputDTO {
    private String idTransacao;
    private String idUsuario;
    private String idCartao;
    private BigDecimal valor;
    private StatusTransacao status;
    private Integer qtdParcelas;
    private String chaveEstabelecimentoComercial;
}
