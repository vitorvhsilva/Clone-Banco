package br.com.bank.payments.domain.service;

import br.com.bank.payments.api.dto.input.PedidoPixInputDTO;
import br.com.bank.payments.api.dto.output.PedidoPixOutputDTO;
import br.com.bank.payments.domain.entity.Pix;
import br.com.bank.payments.domain.repository.CreditoRepository;
import br.com.bank.payments.domain.repository.PixRepository;
import br.com.bank.payments.domain.utils.enums.StatusTransacao;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransacaoService {
    private ModelMapper modelMapper;
    private PixRepository pixRepository;
    private CreditoRepository creditoRepository;

    public ResponseEntity<PedidoPixOutputDTO> fazerPedidoPix(PedidoPixInputDTO dto) {
        Pix pix = modelMapper.map(dto, Pix.class);

        pix.setStatus(StatusTransacao.EM_PROCESSAMENTO);

        PedidoPixOutputDTO pixOutput = modelMapper.map(pixRepository.save(pix), PedidoPixOutputDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(pixOutput);
    }
}
