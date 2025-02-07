package br.com.bank.payments.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transacoes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Credito extends Transacao{
    @Id
    private String idTransacao;
    private Integer qtdParcelas;
    private String chaveEstabelecimentoComercial;
}
