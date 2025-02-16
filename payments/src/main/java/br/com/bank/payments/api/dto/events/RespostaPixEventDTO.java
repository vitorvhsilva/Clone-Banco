package br.com.bank.payments.api.dto.events;

import br.com.bank.payments.domain.utils.enums.StatusResposta;


public class RespostaPixEventDTO {
    private String idTransacao;
    private StatusResposta status;
    private String mensagem;

    public RespostaPixEventDTO(String idTransacao, StatusResposta status, String mensagem) {
        this.idTransacao = idTransacao;
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
}


