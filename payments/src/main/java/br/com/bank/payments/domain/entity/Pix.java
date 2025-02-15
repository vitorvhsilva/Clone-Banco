package br.com.bank.payments.domain.entity;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transacoes")
public class Pix extends Transacao{
    @Id
    private String idTransacao;
    private String chavePix;

    public Pix() {
    }

    public Pix(String idUsuario, BigDecimal valor, StatusTransacao status, String idTransacao) {
        super(idUsuario, valor, status);
        this.idTransacao = idTransacao;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }
}
