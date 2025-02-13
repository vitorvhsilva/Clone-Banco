package br.com.bank.payments.api.listener;

import br.com.bank.payments.api.dto.events.RespostaCreditoEventDTO;
import br.com.bank.payments.domain.entity.Credito;
import br.com.bank.payments.domain.repository.CreditoRepository;
import br.com.bank.payments.domain.utils.enums.StatusResposta;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class RespostaCreditoListener {

    private CreditoRepository creditoRepository;

    @KafkaListener(topics = "resposta-credito-topic", groupId = "resposta-credito-consumer", containerFactory = "respostaCreditoContainerFactory")
    public void processarCredito(RespostaCreditoEventDTO event) {
        Credito credito = creditoRepository.findById(event.getIdTransacao()).get();

        if (event.getStatus().equals(StatusResposta.INVALIDO)) {
            log.error("Credito de id " + event.getIdTransacao() + " voltou com erro!, a mensagem de erro foi: " + event.getMensagem());

            credito.setStatus(StatusTransacao.INVALIDA);
            creditoRepository.save(credito);

            return;
        }

        credito.setStatus(StatusTransacao.VALIDA);
        creditoRepository.save(credito);

        log.info("Credito de id " + credito.getIdTransacao() + " processado e atualizado com sucesso!");
    }
}
