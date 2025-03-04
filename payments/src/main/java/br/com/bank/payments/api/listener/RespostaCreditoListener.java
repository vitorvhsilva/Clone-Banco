package br.com.bank.payments.api.listener;

import br.com.bank.payments.api.dto.events.RespostaCreditoEventDTO;
import br.com.bank.payments.api.dto.events.TransacaoS3DTO;
import br.com.bank.payments.domain.entity.Credito;
import br.com.bank.payments.domain.repository.CreditoRepository;
import br.com.bank.payments.domain.service.S3Service;
import br.com.bank.payments.domain.utils.enums.StatusResposta;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RespostaCreditoListener {

    private final CreditoRepository creditoRepository;
    private final Logger log;
    private final S3Service s3Service;

    public RespostaCreditoListener(CreditoRepository creditoRepository, Logger log, S3Service s3Service) {
        this.creditoRepository = creditoRepository;
        this.log = log;
        this.s3Service = s3Service;
    }

    @KafkaListener(topics = "resposta-credito-topic", groupId = "resposta-credito-consumer", containerFactory = "respostaCreditoContainerFactory")
    public void processarCredito(RespostaCreditoEventDTO event) {
        Credito credito = creditoRepository.findById(event.getIdTransacao()).get();

        TransacaoS3DTO transacaoS3 = new TransacaoS3DTO(event);
        s3Service.salvarTransacaoNoS3(transacaoS3.getIdUsuario(), transacaoS3);

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
