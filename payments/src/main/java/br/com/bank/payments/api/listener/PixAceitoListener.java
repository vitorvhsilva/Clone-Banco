package br.com.bank.payments.api.listener;

import br.com.bank.payments.api.dto.events.PixAceitoEventDTO;
import br.com.bank.payments.domain.entity.Pix;
import br.com.bank.payments.domain.repository.PixRepository;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PixAceitoListener {

    private PixRepository pixRepository;

    @KafkaListener(topics = "pix-aceito-topic", groupId = "pix-aceito-consumer", containerFactory = "pixAceitoContainerFactory")
    public void processarPixAceito(PixAceitoEventDTO event) {
        Pix pix = pixRepository.findById(event.getIdTransacao()).get();

        pix.setStatus(StatusTransacao.VALIDA);
        pixRepository.save(pix);

        log.info("Pix de id " + pix.getIdTransacao() + " processado e atualizado com sucesso!");
    }
}
