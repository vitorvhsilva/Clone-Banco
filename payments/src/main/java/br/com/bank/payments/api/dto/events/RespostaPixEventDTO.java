package br.com.bank.payments.api.dto.events;

import br.com.bank.payments.domain.utils.enums.StatusResposta;
import lombok.Data;

@Data
public class RespostaPixEventDTO {
    private String idTransacao;
    private StatusResposta status;
    private String mensagem;
}
