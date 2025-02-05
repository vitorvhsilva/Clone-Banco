package br.com.bank.payments.domain.entity;

import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
abstract public class Transacao {
    private String idUsuario;
    private BigDecimal valor;
    private StatusTransacao status;
}
