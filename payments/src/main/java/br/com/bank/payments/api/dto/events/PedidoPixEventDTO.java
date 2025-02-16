package br.com.bank.payments.api.dto.events;

import java.math.BigDecimal;


public class PedidoPixEventDTO {
    private String idTransacao;
    private String idUsuario;
    private BigDecimal valor;
    private String chavePix;

    public PedidoPixEventDTO(String idTransacao, String idUsuario, BigDecimal valor, String chavePix) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.chavePix = chavePix;
    }

    public PedidoPixEventDTO() {
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
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