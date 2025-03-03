package br.com.bank.payments.api.controller;

import br.com.bank.payments.api.dto.input.PedidoCreditoInputDTO;
import br.com.bank.payments.api.dto.input.PedidoPixInputDTO;
import br.com.bank.payments.api.dto.output.PedidoCreditoOutputDTO;
import br.com.bank.payments.api.dto.output.PedidoPixOutputDTO;
import br.com.bank.payments.domain.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transacoes")
public class TransacaoController {

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    private final TransacaoService transacaoService;

    @PostMapping("/pix") @Operation(description = "Faz um pedido de pix para o serviço de usuários")
    private ResponseEntity<PedidoPixOutputDTO> fazerPedidoPix(@RequestBody PedidoPixInputDTO dto) {
        return transacaoService.fazerPedidoPix(dto);
    }

    @GetMapping("/pix") @Operation(description = "Obtém todos os pix já feitos")
    private List<PedidoPixOutputDTO> obterPixes(Pageable pageable) {
        return transacaoService.obterPixes(pageable);
    }

    @GetMapping("/pix/{id}") @Operation(description = "Obtém o pix pelo id")
    private ResponseEntity<PedidoPixOutputDTO> obterPixPorId(@PathVariable String id) {
        return transacaoService.obterPixPorId(id);
    }

    @PostMapping("/creditos") @Operation(description = "Faz um pedido de crédito para o serviço de cartões")
    private ResponseEntity<PedidoCreditoOutputDTO> fazerPedidoCredito(@RequestBody PedidoCreditoInputDTO dto) {
        return transacaoService.fazerPedidoCredito(dto);
    }

    @GetMapping("/creditos") @Operation(description = "Obtém todos os créditos feitos")
    private List<PedidoCreditoOutputDTO> obterCreditos(Pageable pageable) {
        return transacaoService.obterCreditos(pageable);
    }

    @GetMapping("/creditos/{id}") @Operation(description = "Obtém o crédito pelo id")
    private ResponseEntity<PedidoCreditoOutputDTO> obterCreditoPeloId(@PathVariable String id) {
        return transacaoService.obterCreditoPeloId(id);
    }
}
