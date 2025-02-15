package br.com.bank.users.api.controller

import br.com.bank.users.api.dto.events.PagarFaturaEventDTO
import br.com.bank.users.api.dto.input.PedidoCartaoInputDTO
import br.com.bank.users.api.dto.input.AtualizarUsuarioDTO
import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.input.PagarFaturaInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.domain.service.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @PostMapping @Operation(description = "Cadastra o usuário na API")
    fun cadastrarUsuario(@RequestBody dto: CadastroUsuarioInputDTO): ResponseEntity<CadastroUsuarioOutputDTO> {
        return usuarioService.cadastrarUsuario(dto)
    }

    @GetMapping@Operation(description = "Obtem todos os usuários do banco de dados")
    fun obterTodosOsUsuarios(pageable: Pageable): Page<ObterUsuarioDTO> {
        return usuarioService.obterTodosOsUsuarios(pageable)
    }

    @GetMapping("/{id}") @Operation(description = "Obtem o usuário pelo id do banco de dados")
    fun obterUsuarioPorId(@PathVariable id: String): ObterUsuarioDetalhadoDTO {
        return usuarioService.obterUsuarioPorId(id)
    }

    @PutMapping@Operation(description = "Atualiza o usuário")
    fun atualizarUsuario(@RequestBody dto: AtualizarUsuarioDTO): ObterUsuarioDetalhadoDTO {
        return usuarioService.atualizarUsuario(dto)
    }

    @PutMapping("/{id}") @Operation(description = "Simula o usuário recebendo seu salário")
    fun usuarioReceberSalario(@PathVariable id: String): ObterUsuarioDTO {
        return usuarioService.usuarioReceberSalario(id)
    }

    @PostMapping("/cartoes") @Operation(description = "Pede um cartão para o serviço de cartões")
    fun fazerPedidoDeCartao(@RequestBody dto: PedidoCartaoInputDTO): ResponseEntity<Void>{
        return usuarioService.fazerPedidoDeCartao(dto)
    }

    @PostMapping("/faturas") @Operation(description = "Paga a fatura no serviço de cartões")
    fun pagarFaturaDoCartao(@RequestBody dto: PagarFaturaInputDTO): PagarFaturaEventDTO {
        return usuarioService.pagarFaturaDoCartao(dto)
    }
}