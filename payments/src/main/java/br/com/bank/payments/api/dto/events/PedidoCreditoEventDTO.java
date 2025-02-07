package br.com.bank.payments.api.dto.events;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoCreditoEventDTO {
    private String idTransacao;
    private String idUsuario;
    private BigDecimal valor;
    private Integer qtdParcelas;
    private String chaveEstabelecimentoComercial;
}
