package br.com.bank.payments.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoCreditoInputDTO {
    @NotBlank
    private String idUsuario;
    @NotBlank
    private String idCartao;
    @NotNull
    private BigDecimal valor;
    @NotNull
    private Integer qtdParcelas;
    @NotBlank
    private String chaveEstabelecimentoComercial;
}
