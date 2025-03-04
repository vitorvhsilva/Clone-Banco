package br.com.bank.payments.api.dto.events;

import br.com.bank.payments.domain.utils.enums.StatusResposta;

import java.math.BigDecimal;


public class RespostaPixEventDTO {
    private String idTransacao;
    private String idUsuario;
    private BigDecimal valor;
    private StatusResposta status;
    private String mensagem;

    public RespostaPixEventDTO(String idTransacao, String idUsuario, BigDecimal valor, StatusResposta status, String mensagem) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.status = status;
        this.mensagem = mensagem;
    }

    public RespostaPixEventDTO() {
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public StatusResposta getStatus() {
        return status;
    }

    public void setStatus(StatusResposta status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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
}


