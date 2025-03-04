package br.com.bank.payments.api.listener;

import br.com.bank.payments.api.dto.events.RespostaPixEventDTO;
import br.com.bank.payments.api.dto.events.TransacaoS3DTO;
import br.com.bank.payments.domain.entity.Pix;
import br.com.bank.payments.domain.repository.PixRepository;
import br.com.bank.payments.domain.service.S3Service;
import br.com.bank.payments.domain.utils.enums.StatusResposta;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RespostaPixListener {

    private final PixRepository pixRepository;
    private final Logger log;
    private final S3Service s3Service;

    public RespostaPixListener(PixRepository pixRepository, Logger log, S3Service s3Service) {
        this.pixRepository = pixRepository;
        this.log = log;
        this.s3Service = s3Service;
    }

    @KafkaListener(topics = "resposta-pix-topic", groupId = "resposta-pix-consumer", containerFactory = "respostaPixContainerFactory")
    public void processarPix(RespostaPixEventDTO event) {
        Pix pix = pixRepository.findById(event.getIdTransacao()).get();

        TransacaoS3DTO transacaoS3 = new TransacaoS3DTO(event);
        s3Service.salvarTransacaoNoS3(transacaoS3.getIdUsuario(), transacaoS3);

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
