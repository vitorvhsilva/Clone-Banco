package br.com.bank.payments.api.dto.events;

import java.math.BigDecimal;

public class PedidoCreditoEventDTO {
    private String idTransacao;
    private String idUsuario;
    private String idCartao;
    private BigDecimal valor;
    private Integer qtdParcelas;
    private String chaveEstabelecimentoComercial;

    public PedidoCreditoEventDTO(String idTransacao, String idUsuario, String idCartao, BigDecimal valor, Integer qtdParcelas, String chaveEstabelecimentoComercial) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.idCartao = idCartao;
        this.valor = valor;
        this.qtdParcelas = qtdParcelas;
        this.chaveEstabelecimentoComercial = chaveEstabelecimentoComercial;
    }

    public PedidoCreditoEventDTO() {
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
