package br.com.bank.users.api.controller

import br.com.bank.users.api.dto.input.AtualizarUsuarioDTO
import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.domain.service.UsuarioService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @PostMapping
    fun cadastrarUsuario(@RequestBody dto: CadastroUsuarioInputDTO): ResponseEntity<CadastroUsuarioOutputDTO> {
        return usuarioService.cadastrarUsuario(dto)
    }

    @GetMapping
    fun obterTodosOsUsuarios(pageable: Pageable): Page<ObterUsuarioDTO> {
        return usuarioService.obterTodosOsUsuarios(pageable)
    }

    @GetMapping("/{id}")
    fun obterUsuarioPorId(@PathVariable id: String): ObterUsuarioDetalhadoDTO {
        return usuarioService.obterUsuarioPorId(id)
    }

    @PatchMapping
    fun atualizarUsuario(@RequestBody dto: AtualizarUsuarioDTO): ObterUsuarioDetalhadoDTO {
        return usuarioService.atualizarUsuario(dto)
    }
}