package br.com.bank.payments.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transacoes")
public class Credito extends Transacao{

    @Id
    private String idTransacao;
    private String idCartao;
    private Integer qtdParcelas;
    private String chaveEstabelecimentoComercial;

    public Credito() {
    }

    public Credito(String idTransacao, String idCartao, Integer qtdParcelas, String chaveEstabelecimentoComercial) {
        this.idTransacao = idTransacao;
        this.idCartao = idCartao;
        this.qtdParcelas = qtdParcelas;
        this.chaveEstabelecimentoComercial = chaveEstabelecimentoComercial;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public String getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(String idCartao) {
        this.idCartao = idCartao;
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
