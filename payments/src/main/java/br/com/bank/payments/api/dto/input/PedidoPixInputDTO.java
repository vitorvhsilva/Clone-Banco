package br.com.bank.payments.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoPixInputDTO {
    @NotBlank
    private String idUsuario;
    @NotNull
    private BigDecimal valor;
    @NotBlank
    private String chavePix;
}
