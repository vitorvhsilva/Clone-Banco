package br.com.bank.payments.api.dto.output;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;

import java.math.BigDecimal;

public class PedidoCreditoOutputDTO {
    private String idTransacao;
    private String idUsuario;
    private String idCartao;
    private BigDecimal valor;
    private StatusTransacao status;
    private Integer qtdParcelas;
    private String chaveEstabelecimentoComercial;

    public PedidoCreditoOutputDTO(String idTransacao, String idUsuario, String idCartao, BigDecimal valor, StatusTransacao status, Integer qtdParcelas, String chaveEstabelecimentoComercial) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.idCartao = idCartao;
        this.valor = valor;
        this.status = status;
        this.qtdParcelas = qtdParcelas;
        this.chaveEstabelecimentoComercial = chaveEstabelecimentoComercial;
    }

    public PedidoCreditoOutputDTO() {
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

    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
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
