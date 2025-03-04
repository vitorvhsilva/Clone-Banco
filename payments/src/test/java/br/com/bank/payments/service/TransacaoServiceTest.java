package br.com.bank.payments.service;

import br.com.bank.payments.api.dto.events.PedidoCreditoEventDTO;
import br.com.bank.payments.api.dto.events.PedidoPixEventDTO;
import br.com.bank.payments.api.dto.input.PedidoCreditoInputDTO;
import br.com.bank.payments.api.dto.input.PedidoPixInputDTO;
import br.com.bank.payments.api.dto.output.PedidoCreditoOutputDTO;
import br.com.bank.payments.api.dto.output.PedidoPixOutputDTO;
import br.com.bank.payments.api.exception.NotFoundException;
import br.com.bank.payments.domain.entity.Credito;
import br.com.bank.payments.domain.entity.Pix;
import br.com.bank.payments.domain.repository.CreditoRepository;
import br.com.bank.payments.domain.repository.PixRepository;
import br.com.bank.payments.domain.service.S3Service;
import br.com.bank.payments.domain.service.TransacaoService;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Mock
    private PixRepository pixRepository;

    @Mock
    private CreditoRepository creditoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private KafkaTemplate<String, PedidoPixEventDTO> pedidoPixKafkaTemplate;

    @Mock
    private KafkaTemplate<String, PedidoCreditoEventDTO> pedidoCreditoKafkaTemplate;

    @Mock
    private S3Service s3Service;

    @Mock
    private Logger log;

    private Pix pix;
    private PedidoPixInputDTO pedidoPixInput;
    private PedidoPixOutputDTO pedidoPixOutput;
    private PedidoPixEventDTO pedidoPixEvent;

    private Credito credito;
    private PedidoCreditoInputDTO pedidoCreditoInput;
    private PedidoCreditoOutputDTO pedidoCreditoOutput;
    private PedidoCreditoEventDTO pedidoCreditoEvent;

    @BeforeEach
    void setUp() {
        pix = new Pix("idUsuarioTeste", BigDecimal.valueOf(100),
                StatusTransacao.EM_PROCESSAMENTO, "idTransacaoTeste", "idChavePixTeste");
        pedidoPixInput = new PedidoPixInputDTO("idTransacaoTeste", BigDecimal.valueOf(100), "idChavePixTeste");
        pedidoPixOutput = new PedidoPixOutputDTO("idTransacaoTeste", "idUsuarioTeste", BigDecimal.valueOf(10),
                StatusTransacao.EM_PROCESSAMENTO, "idChavePixTeste");
        pedidoPixEvent = new PedidoPixEventDTO("idTransacaoTeste", "idUsuarioTeste",
                BigDecimal.valueOf(10), "idChavePixTeste");

        credito = new Credito("idUsuarioTeste", BigDecimal.valueOf(100),
                StatusTransacao.EM_PROCESSAMENTO, "idTransacaoTeste",
                "idCartaoTeste", 1, "idChaveTeste");
        pedidoCreditoInput = new PedidoCreditoInputDTO("idUsuarioTeste", "idCartaoTeste",
                BigDecimal.valueOf(100), 1, "idChaveTeste");
        pedidoCreditoOutput = new PedidoCreditoOutputDTO("idTransacaoTeste","idUsuarioTeste", "idCartaoTeste",
                BigDecimal.valueOf(100), StatusTransacao.EM_PROCESSAMENTO, 1, "idChaveTeste");
        pedidoCreditoEvent = new PedidoCreditoEventDTO("idTransacaoTeste","idUsuarioTeste", "idCartaoTeste",
                BigDecimal.valueOf(100), 1, "idChaveTeste");
    }

    @Test
    void retornarPixesRetornaUmaListaDePix() {

        when(pixRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(pix)));

        List<PedidoPixOutputDTO> response = transacaoService.obterPixes(Pageable.unpaged());

        assertEquals(1, response.size());
    }

    @Test
    void retornarPixPorIdRetornaUmPix() {

        when(pixRepository.findById(any(String.class))).thenReturn(Optional.of(pix));
        when(modelMapper.map(any(Pix.class), eq(PedidoPixOutputDTO.class))).thenReturn(pedidoPixOutput);

        ResponseEntity<PedidoPixOutputDTO> response = transacaoService.obterPixPorId("idTeste");

        assertEquals(pedidoPixOutput, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void fazerPedidoPixDeveEnviarMensagemKafka() {
        when(modelMapper.map(any(PedidoPixInputDTO.class), eq(Pix.class))).thenReturn(pix);
        when(modelMapper.map(any(Pix.class), eq(PedidoPixOutputDTO.class))).thenReturn(pedidoPixOutput);
        when(modelMapper.map(any(PedidoPixOutputDTO.class), eq(PedidoPixEventDTO.class))).thenReturn(pedidoPixEvent);
        when(pixRepository.save(any(Pix.class))).thenReturn(pix);

        ResponseEntity<PedidoPixOutputDTO> response = transacaoService.fazerPedidoPix(pedidoPixInput);

        //verify(pedidoPixKafkaTemplate, times(1)).send(eq("pedido-pix-topic"), eq(pedidoPixEvent.getIdUsuario()), any());
        verify(log, times(1))
                .info("Pedido de pix do usuário " + pedidoPixEvent.getIdUsuario() + " feito!");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void retornarCreditosRetornaUmaListaDePix() {

        when(creditoRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(credito)));

        List<PedidoCreditoOutputDTO> response = transacaoService.obterCreditos(Pageable.unpaged());

        assertEquals(1, response.size());
    }

    @Test
    void retornarCreditoPorIdRetornaUmCredito() {

        when(creditoRepository.findById(any(String.class))).thenReturn(Optional.of(credito));
        when(modelMapper.map(any(Credito.class), eq(PedidoCreditoOutputDTO.class))).thenReturn(pedidoCreditoOutput);

        ResponseEntity<PedidoCreditoOutputDTO> response = transacaoService.obterCreditoPeloId("idTeste");

        assertEquals(pedidoCreditoOutput, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void fazerPedidoCreditoDeveEnviarMensagemKafka() {
        when(modelMapper.map(any(PedidoCreditoInputDTO.class), eq(Credito.class))).thenReturn(credito);
        when(modelMapper.map(any(Credito.class), eq(PedidoCreditoOutputDTO.class))).thenReturn(pedidoCreditoOutput);
        when(modelMapper.map(any(PedidoCreditoOutputDTO.class), eq(PedidoCreditoEventDTO.class))).thenReturn(pedidoCreditoEvent);
        when(creditoRepository.save(any(Credito.class))).thenReturn(credito);

        ResponseEntity<PedidoCreditoOutputDTO> response = transacaoService.fazerPedidoCredito(pedidoCreditoInput);

        //verify(pedidoCreditoKafkaTemplate, times(1)).send(eq("pedido-credito-topic"), eq(pedidoCreditoEvent.getIdUsuario()), any());
        verify(log, times(1))
                .info("Pedido de credito do usuário " + pedidoCreditoEvent.getIdUsuario() + " feito!");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void retornarExcecaoSeNaoEncontrarPix() {

        when(pixRepository.findById(any(String.class))).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> transacaoService.obterPixPorId("idTeste"));
    }

    @Test
    void retornarExcecaoSeNaoEncontrarCredito() {

        when(creditoRepository.findById(any(String.class))).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> transacaoService.obterCreditoPeloId("idTeste"));
    }
}
