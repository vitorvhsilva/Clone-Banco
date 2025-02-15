package br.com.bank.payments.domain.entity;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;

import java.math.BigDecimal;

abstract public class Transacao {
    private String idUsuario;
    private BigDecimal valor;
    private StatusTransacao status;

    public Transacao() {
    }

    public Transacao(String idUsuario, BigDecimal valor, StatusTransacao status) {
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.status = status;
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
}
