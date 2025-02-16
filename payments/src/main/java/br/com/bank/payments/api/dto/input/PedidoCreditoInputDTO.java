package br.com.bank.payments.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


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

    public PedidoCreditoInputDTO(String idUsuario, String idCartao, BigDecimal valor, Integer qtdParcelas, String chaveEstabelecimentoComercial) {
        this.idUsuario = idUsuario;
        this.idCartao = idCartao;
        this.valor = valor;
        this.qtdParcelas = qtdParcelas;
        this.chaveEstabelecimentoComercial = chaveEstabelecimentoComercial;
    }

    public PedidoCreditoInputDTO() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(String idCartao) {
        this.idCartao = idCartao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public String getChaveEstabelecimentoComercial() {
        return chaveEstabelecimentoComercial;
    }

    public void setChaveEstabelecimentoComercial(String chaveEstabelecimentoComercial) {
        this.chaveEstabelecimentoComercial = chaveEstabelecimentoComercial;
    }
}
