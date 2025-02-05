package br.com.bank.payments.api.dto.input;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoPixInputDTO {
    private String idUsuario;
    private BigDecimal valor;
    private String chavePix;
}
