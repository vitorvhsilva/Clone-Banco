package br.com.bank.payments.api.listener;

import br.com.bank.payments.api.dto.events.RespostaPixEventDTO;
import br.com.bank.payments.domain.entity.Pix;
import br.com.bank.payments.domain.repository.PixRepository;
import br.com.bank.payments.domain.utils.enums.StatusResposta;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RespostaPixListener {

    private final PixRepository pixRepository;
    private final Logger log;

    public RespostaPixListener(PixRepository pixRepository, Logger log) {
        this.pixRepository = pixRepository;
        this.log = log;
    }

    @KafkaListener(topics = "resposta-pix-topic", groupId = "resposta-pix-consumer", containerFactory = "respostaPixContainerFactory")
    public void processarPix(RespostaPixEventDTO event) {
        Pix pix = pixRepository.findById(event.getIdTransacao()).get();

        if (event.getStatus().equals(StatusResposta.INVALIDO)) {
            log.error("Pix de id " + event.getIdTransacao() + " voltou com erro!, a mensagem de erro foi: " + event.getMensagem());

            pix.setStatus(StatusTransacao.INVALIDA);
            pixRepository.save(pix);

            return;
        }

        pix.setStatus(StatusTransacao.VALIDA);
        pixRepository.save(pix);

        log.info("Pix de id " + pix.getIdTransacao() + " processado e atualizado com sucesso!");
    }
}
