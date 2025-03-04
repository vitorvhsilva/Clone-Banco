package br.com.bank.payments.api.controller;

import br.com.bank.payments.api.dto.events.TransacaoS3DTO;
import br.com.bank.payments.api.dto.input.PedidoCreditoInputDTO;
import br.com.bank.payments.api.dto.input.PedidoPixInputDTO;
import br.com.bank.payments.api.dto.output.PedidoCreditoOutputDTO;
import br.com.bank.payments.api.dto.output.PedidoPixOutputDTO;
import br.com.bank.payments.domain.service.S3Service;
import br.com.bank.payments.domain.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transacoes")
public class TransacaoController {

    public TransacaoController(TransacaoService transacaoService, S3Service s3Service) {
        this.transacaoService = transacaoService;
        this.s3Service = s3Service;
    }

    private final TransacaoService transacaoService;
    private final S3Service s3Service;

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

    @PostMapping("/s3")
    private void testeS3(@RequestBody TransacaoS3DTO dto) {
        s3Service.salvarTransacaoNoS3(dto.getIdUsuario(), dto);
    }
}
