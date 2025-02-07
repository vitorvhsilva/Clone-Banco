package br.com.bank.payments.domain.service;

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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TransacaoService {
    private ModelMapper modelMapper;
    private PixRepository pixRepository;
    private CreditoRepository creditoRepository;
    private KafkaTemplate<String, PedidoPixEventDTO> pedidoPixKafkaTemplate;
    private KafkaTemplate<String, PedidoCreditoEventDTO> pedidoCreditoKafkaTemplate;

    public ResponseEntity<PedidoPixOutputDTO> fazerPedidoPix(PedidoPixInputDTO dto) {
        Pix pix = modelMapper.map(dto, Pix.class);

        pix.setStatus(StatusTransacao.EM_PROCESSAMENTO);

        PedidoPixOutputDTO pixOutput = modelMapper.map(pixRepository.save(pix), PedidoPixOutputDTO.class);
        PedidoPixEventDTO pixEvent = modelMapper.map(pixOutput, PedidoPixEventDTO.class);

        pedidoPixKafkaTemplate.send("pedido-pix-topic", pixEvent.getIdUsuario(), pixEvent);
        log.info("Pedido de pix do usuário " + pixEvent.getIdUsuario() + " feito!");

        return ResponseEntity.status(HttpStatus.CREATED).body(pixOutput);
    }

    public List<PedidoPixOutputDTO> obterPixes(Pageable pageable) {
        Page<Pix> pixes = pixRepository.findAll(pageable);

        return pixes.stream()
                .map(p -> modelMapper.map(p, PedidoPixOutputDTO.class))
                .toList();
    }

    public ResponseEntity<PedidoPixOutputDTO> obterPixPorId(String id) {
        Pix pix = pixRepository.findById(id).orElseThrow(() -> new NotFoundException("Pix não encontrado!"));

        return ResponseEntity.ok(modelMapper.map(pix, PedidoPixOutputDTO.class));
    }

    public ResponseEntity<PedidoCreditoOutputDTO> fazerPedidoCredito(PedidoCreditoInputDTO dto) {
        Credito credito = modelMapper.map(dto, Credito.class);
        credito.setStatus(StatusTransacao.EM_PROCESSAMENTO);

        PedidoCreditoOutputDTO creditoOutput = modelMapper.map(creditoRepository.save(credito), PedidoCreditoOutputDTO.class);
        PedidoCreditoEventDTO creditoEvent = modelMapper.map(creditoOutput, PedidoCreditoEventDTO.class);

        pedidoCreditoKafkaTemplate.send("pedido-credito-topic", creditoEvent.getIdUsuario(), creditoEvent);
        log.info("Pedido de credito do usuário " + creditoEvent.getIdUsuario() + " feito!");

        return ResponseEntity.status(HttpStatus.CREATED).body(creditoOutput);
    }
}
