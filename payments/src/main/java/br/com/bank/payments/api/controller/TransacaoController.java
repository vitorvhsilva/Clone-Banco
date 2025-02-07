package br.com.bank.payments.api.controller;

import br.com.bank.payments.api.dto.input.PedidoPixInputDTO;
import br.com.bank.payments.api.dto.output.PedidoPixOutputDTO;
import br.com.bank.payments.domain.service.TransacaoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transacoes")
@AllArgsConstructor
public class TransacaoController {

    private TransacaoService transacaoService;

    @PostMapping("/pix")
    private ResponseEntity<PedidoPixOutputDTO> fazerPedidoPix(@RequestBody PedidoPixInputDTO dto) {
        return transacaoService.fazerPedidoPix(dto);
    }

    @GetMapping("/pix")
    private List<PedidoPixOutputDTO> obterPixes(Pageable pageable) {
        return transacaoService.obterPixes(pageable);
    }

    @GetMapping("/pix/{id}")
    private ResponseEntity<PedidoPixOutputDTO> obterPixPorId(@PathVariable String id) {
        return transacaoService.obterPixPorId(id);
    }
}
