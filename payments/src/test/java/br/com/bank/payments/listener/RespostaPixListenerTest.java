package br.com.bank.payments.listener;

import br.com.bank.payments.api.dto.events.RespostaPixEventDTO;
import br.com.bank.payments.api.listener.RespostaPixListener;
import br.com.bank.payments.domain.entity.Pix;
import br.com.bank.payments.domain.repository.PixRepository;
import br.com.bank.payments.domain.service.S3Service;
import br.com.bank.payments.domain.utils.enums.StatusResposta;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RespostaPixListenerTest {

    @Mock
    private PixRepository pixRepository;

    @Mock
    private Logger log;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private RespostaPixListener respostaPixListener;

    private Pix pix;
    private RespostaPixEventDTO eventoValido;
    private RespostaPixEventDTO eventoInvalido;

    @BeforeEach
    void setUp() {
         pix = new Pix("idUsuarioTeste", BigDecimal.valueOf(100),
                StatusTransacao.EM_PROCESSAMENTO, "idTransacaoTeste", "idChavePixTeste");
        eventoValido = new RespostaPixEventDTO("idTransacaoTeste", "idUsuarioTeste", BigDecimal.valueOf(100),
                StatusResposta.VALIDO, "OK");
        eventoInvalido = new RespostaPixEventDTO("idTransacaoTeste", "idUsuarioTeste", BigDecimal.valueOf(100),
                StatusResposta.INVALIDO, "ERRO");
    }

    @Test
    void processarPixValido() {
        when(pixRepository.findById(any(String.class))).thenReturn(Optional.of(pix));

        respostaPixListener.processarPix(eventoValido);

        verify(log, times(1))
                .info("Pix de id " + pix.getIdTransacao() + " processado e atualizado com sucesso!");
        assertEquals(StatusTransacao.VALIDA, pix.getStatus());
    }

    @Test
    void processarPixInvalido() {
        when(pixRepository.findById(any(String.class))).thenReturn(Optional.of(pix));

        respostaPixListener.processarPix(eventoInvalido);

        verify(log, times(1))
                .error("Pix de id " + eventoInvalido.getIdTransacao() + " voltou com erro!, a mensagem de erro foi: " + eventoInvalido.getMensagem());
        assertEquals(StatusTransacao.INVALIDA, pix.getStatus());
    }
}
