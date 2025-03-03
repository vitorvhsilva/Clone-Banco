package br.com.bank.payments.listener;

import br.com.bank.payments.api.dto.events.PedidoCreditoEventDTO;
import br.com.bank.payments.api.dto.events.RespostaCreditoEventDTO;
import br.com.bank.payments.api.listener.RespostaCreditoListener;
import br.com.bank.payments.domain.entity.Credito;
import br.com.bank.payments.domain.repository.CreditoRepository;
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
public class RespostaCreditoListenerTest {

    @Mock
    private CreditoRepository creditoRepository;

    @Mock
    private Logger log;

    @InjectMocks
    private RespostaCreditoListener respostaCreditoListener;

    private Credito credito;
    private RespostaCreditoEventDTO eventoValido;
    private RespostaCreditoEventDTO eventoInvalido;

    @BeforeEach
    void setUp() {
        credito = new Credito("idUsuarioTeste", BigDecimal.valueOf(100),
                StatusTransacao.EM_PROCESSAMENTO, "idTransacaoTeste",
                "idCartaoTeste", 1, "idChaveTeste");
        eventoValido = new RespostaCreditoEventDTO("idTransacaoTeste",
                StatusResposta.VALIDO, "OK");
        eventoInvalido = new RespostaCreditoEventDTO("idTransacaoTeste",
                StatusResposta.INVALIDO, "ERRO");
    }

    @Test
    void processarCreditoValido() {
        when(creditoRepository.findById(any(String.class))).thenReturn(Optional.of(credito));

        respostaCreditoListener.processarCredito(eventoValido);

        verify(log, times(1))
                .info("Credito de id " + credito.getIdTransacao() + " processado e atualizado com sucesso!");
        assertEquals(StatusTransacao.VALIDA, credito.getStatus());
    }

    @Test
    void processarCreditoInvalido() {
        when(creditoRepository.findById(any(String.class))).thenReturn(Optional.of(credito));

        respostaCreditoListener.processarCredito(eventoInvalido);

        verify(log, times(1))
                .error("Credito de id " + eventoInvalido.getIdTransacao() + " voltou com erro!, a mensagem de erro foi: " + eventoInvalido.getMensagem());
        assertEquals(StatusTransacao.INVALIDA, credito.getStatus());
    }
}
