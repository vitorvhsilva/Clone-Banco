package br.com.bank.payments.api.dto.events;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import br.com.bank.payments.domain.utils.enums.TipoTransacao;

import java.math.BigDecimal;

public class TransacaoS3DTO {
    private String idTransacao;
    private String idUsuario;
    private BigDecimal valor;
    private StatusTransacao status;
    private TipoTransacao tipo;

    public TransacaoS3DTO() {
    }

    public TransacaoS3DTO(String idTransacao, String idUsuario, BigDecimal valor, StatusTransacao status, TipoTransacao tipo) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.status = status;
        this.tipo = tipo;
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

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

}
