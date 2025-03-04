package br.com.bank.payments.api.dto.events;

import br.com.bank.payments.domain.utils.enums.StatusResposta;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import br.com.bank.payments.domain.utils.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransacaoS3DTO {
    private String idTransacao;
    private String idUsuario;
    private BigDecimal valor;
    private StatusTransacao status;
    private TipoTransacao tipo;
    private LocalDateTime dataCriacao;

    public TransacaoS3DTO() {
    }

    public TransacaoS3DTO(PedidoPixEventDTO dto) {
        this.idTransacao = dto.getIdTransacao();
        this.idUsuario = dto.getIdUsuario();
        this.valor = dto.getValor();
        this.status = StatusTransacao.EM_PROCESSAMENTO;
        this.tipo = TipoTransacao.PIX;
        this.dataCriacao = LocalDateTime.now();
    }

    public TransacaoS3DTO(PedidoCreditoEventDTO dto) {
        this.idTransacao = dto.getIdTransacao();
        this.idUsuario = dto.getIdUsuario();
        this.valor = dto.getValor();
        this.status = StatusTransacao.EM_PROCESSAMENTO;
        this.tipo = TipoTransacao.CREDITO;
        this.dataCriacao = LocalDateTime.now();
    }

    public TransacaoS3DTO(RespostaPixEventDTO dto) {
        this.idTransacao = dto.getIdTransacao();
        this.idUsuario = dto.getIdUsuario();
        this.valor = dto.getValor();
        this.status = dto.getStatus() == StatusResposta.VALIDO ? StatusTransacao.VALIDA : StatusTransacao.INVALIDA;
        this.tipo = TipoTransacao.PIX;
        this.dataCriacao = LocalDateTime.now();
    }

    public TransacaoS3DTO(RespostaCreditoEventDTO dto) {
        this.idTransacao = dto.getIdTransacao();
        this.idUsuario = dto.getIdUsuario();
        this.valor = dto.getValor();
        this.status = dto.getStatus() == StatusResposta.VALIDO ? StatusTransacao.VALIDA : StatusTransacao.INVALIDA;
        this.tipo = TipoTransacao.CREDITO;
        this.dataCriacao = LocalDateTime.now();
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
