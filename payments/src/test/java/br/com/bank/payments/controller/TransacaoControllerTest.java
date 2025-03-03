package br.com.bank.payments.controller;

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
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PixRepository pixRepository;

    @MockitoBean
    private CreditoRepository creditoRepository;

    @MockitoBean
    private ModelMapper modelMapper;

    private Pix pix;
    private Credito credito;
    private PedidoPixOutputDTO pedidoPixOutput;
    private PedidoPixInputDTO pedidoPixInput;
    private PedidoPixEventDTO pedidoPixEvent;
    private PedidoCreditoInputDTO pedidoCreditoInput;
    private PedidoCreditoOutputDTO pedidoCreditoOutput;
    private PedidoCreditoEventDTO pedidoCreditoEvent;

    @BeforeEach
    void setUp() {
        pix = new Pix("1", BigDecimal.valueOf(10), StatusTransacao.EM_PROCESSAMENTO, "1", "1");
        credito = new Credito("1", BigDecimal.valueOf(10), StatusTransacao.EM_PROCESSAMENTO, "1", "1", 1, "teste");
        pedidoPixOutput = new PedidoPixOutputDTO("1", "1", BigDecimal.valueOf(10), StatusTransacao.EM_PROCESSAMENTO, "1");
        pedidoPixInput = new PedidoPixInputDTO("1", BigDecimal.valueOf(10), "");
        pedidoPixEvent = new PedidoPixEventDTO("1", "1", BigDecimal.valueOf(10), "1");
        pedidoCreditoInput = new PedidoCreditoInputDTO("", "", BigDecimal.valueOf(10), 1, "");
        pedidoCreditoOutput = new PedidoCreditoOutputDTO("", "", "", BigDecimal.valueOf(10), StatusTransacao.EM_PROCESSAMENTO, 1, "");
        pedidoCreditoEvent = new PedidoCreditoEventDTO("", "", "", BigDecimal.valueOf(10), 1, "");
    }

    @Test
    void deveRetornar200ParaRetornarTodosOsPix() throws Exception {
        List<Pix> pixes = List.of(
                pix
        );

        Page<Pix> pagePix = new PageImpl<>(pixes);

        when(pixRepository.findAll(any(Pageable.class))).thenReturn(pagePix);

        MockHttpServletResponse response = mockMvc.perform(
                get("/transacoes/pix")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveRetornar200ParaRetornarTodosOsCreditos() throws Exception {
        List<Credito> creditos = List.of(
               credito
        );

        Page<Credito> pageCreditos = new PageImpl<>(creditos);

        when(creditoRepository.findAll(any(Pageable.class))).thenReturn(pageCreditos);

        MockHttpServletResponse response = mockMvc.perform(
                get("/transacoes/creditos")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveRetornar200ParaRetornarPixPorId() throws Exception {
        String id = "1";

        when(pixRepository.findById(id)).thenReturn(Optional.of(pix));

        MockHttpServletResponse response = mockMvc.perform(
                get("/transacoes/pix/{id}", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveRetornar400ParaRetornarPixInexistente() throws Exception {
        String id = "1";

        when(pixRepository.findById(id)).thenThrow(NotFoundException.class);

        MockHttpServletResponse response = mockMvc.perform(
                get("/transacoes/pix/{id}", id)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveRetornar200ParaRetornarCreditoPorId() throws Exception {
        String id = "1";

        when(creditoRepository.findById(id)).thenReturn(Optional.of(credito));

        MockHttpServletResponse response = mockMvc.perform(
                get("/transacoes/creditos/{id}", id)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveRetornar400ParaRetornarCreditoInexistente() throws Exception {
        String id = "1";

        when(creditoRepository.findById(id)).thenThrow(NotFoundException.class);

        MockHttpServletResponse response = mockMvc.perform(
                get("/transacoes/creditos/{id}", id)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }

    @Test
    void deveRetornar200ParaPedidoDePix() throws Exception {
        String json = """
                {
                    "idUsuario": "1",
                    "valor": 10,
                    "chavePix": "1"
                }
                """;

        when(modelMapper.map(any(PedidoPixInputDTO.class), eq(Pix.class))).thenReturn(pix);
        when(modelMapper.map(any(Pix.class), eq(PedidoPixOutputDTO.class))).thenReturn(pedidoPixOutput);
        when(modelMapper.map(any(PedidoPixOutputDTO.class), eq(PedidoPixEventDTO.class))).thenReturn(pedidoPixEvent);
        when(pixRepository.save(any(Pix.class))).thenReturn(pix);

        MockHttpServletResponse response = mockMvc.perform(
                post("/transacoes/pix")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(201, response.getStatus());
    }

    @Test
    void deveRetornar200ParaPedidoDeCredito() throws Exception {
        String json = """
                {
                    "idCartao": "",
                    "idUsuario": "",
                    "valor": 10,
                    "qtdParcelas": 1,
                    "chaveEstabelecimentoComercial": ""
                }
                """;

        when(modelMapper.map(any(PedidoCreditoInputDTO.class), eq(Credito.class))).thenReturn(credito);
        when(modelMapper.map(any(Credito.class), eq(PedidoCreditoOutputDTO.class))).thenReturn(pedidoCreditoOutput);
        when(modelMapper.map(any(PedidoCreditoOutputDTO.class), eq(PedidoCreditoEventDTO.class))).thenReturn(pedidoCreditoEvent);
        when(creditoRepository.save(any(Credito.class))).thenReturn(credito);

        MockHttpServletResponse response = mockMvc.perform(
                post("/transacoes/creditos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(201, response.getStatus());
    }
}