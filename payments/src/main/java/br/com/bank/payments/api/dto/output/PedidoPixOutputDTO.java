package br.com.bank.payments.api.dto.output;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;

import java.math.BigDecimal;

public class PedidoPixOutputDTO {
    private String idTransacao;
    private String idUsuario;
    private BigDecimal valor;
    private StatusTransacao status;
    private String chavePix;

    public PedidoPixOutputDTO(String idTransacao, String idUsuario, BigDecimal valor, StatusTransacao status, String chavePix) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.status = status;
        this.chavePix = chavePix;
    }

    public PedidoPixOutputDTO() {
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

    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }
}
