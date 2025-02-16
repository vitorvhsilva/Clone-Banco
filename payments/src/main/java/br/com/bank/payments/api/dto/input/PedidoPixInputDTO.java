package br.com.bank.payments.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class PedidoPixInputDTO {
    @NotBlank
    private String idUsuario;
    @NotNull
    private BigDecimal valor;
    @NotBlank
    private String chavePix;

    public PedidoPixInputDTO(String idUsuario, BigDecimal valor, String chavePix) {
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.chavePix = chavePix;
    }

    public PedidoPixInputDTO() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }
}
