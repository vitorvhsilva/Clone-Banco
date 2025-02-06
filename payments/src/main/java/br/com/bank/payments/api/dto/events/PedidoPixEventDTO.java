package br.com.bank.payments.api.dto.events;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoPixEventDTO {
    private String idUsuario;
    private BigDecimal valor;
    private String chavePix;
}
