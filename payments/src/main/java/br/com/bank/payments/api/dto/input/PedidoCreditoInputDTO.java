package br.com.bank.payments.api.dto.input;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoCreditoInputDTO {
    private String idUsuario;
    private BigDecimal valor;
    private Integer qtdParcelas;
    private String chaveEstabelecimentoComercial;
}
