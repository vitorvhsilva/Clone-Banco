package br.com.bank.payments.api.listener;

import br.com.bank.payments.api.dto.events.RespostaCreditoEventDTO;
import br.com.bank.payments.domain.entity.Credito;
import br.com.bank.payments.domain.repository.CreditoRepository;
import br.com.bank.payments.domain.utils.enums.StatusResposta;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RespostaCreditoListener {

    private final CreditoRepository creditoRepository;
    private final Logger log;

    public RespostaCreditoListener(CreditoRepository creditoRepository, Logger log) {
        this.creditoRepository = creditoRepository;
        this.log = log;
    }

    @KafkaListener(topics = "resposta-credito-topic", groupId = "resposta-credito-consumer", containerFactory = "respostaCreditoContainerFactory")
    public void processarCredito(RespostaCreditoEventDTO event) {
        Credito credito = creditoRepository.findById(event.idTransacao()).get();

        if (event.status().equals(StatusResposta.INVALIDO)) {
            log.error("Credito de id " + event.idTransacao() + " voltou com erro!, a mensagem de erro foi: " + event.mensagem());

            credito.setStatus(StatusTransacao.INVALIDA);
            creditoRepository.save(credito);

            return;
        }

        credito.setStatus(StatusTransacao.VALIDA);
        creditoRepository.save(credito);

        log.info("Credito de id " + credito.getIdTransacao() + " processado e atualizado com sucesso!");
    }
}
