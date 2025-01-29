package br.com.bank.users.domain.service

import br.com.bank.users.api.dto.input.AtualizarUsuarioDTO
import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.service.strategy.SegmentoStrategy
import br.com.bank.users.domain.utils.mappers.UsuarioMapper
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UsuarioService (
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapperImpl: UsuarioMapper,
    private val strategys: List<SegmentoStrategy>
) {
    fun cadastrarUsuario(dto: CadastroUsuarioInputDTO): ResponseEntity<CadastroUsuarioOutputDTO> {
        var usuario: Usuario = usuarioMapperImpl.cadastroInputParaEntidade(dto)

        injetarAgenciaeConta(usuario)
        injetarSegmento(usuario)

        usuario = usuarioRepository.save(usuario)

        val usuarioOutput: CadastroUsuarioOutputDTO = usuarioMapperImpl.entidadeParaCadastroOutput(usuario)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usuarioOutput)
    }

    private fun injetarAgenciaeConta(usuario: Usuario) {
        var agencia: String
        var conta: String
        do {
            agencia = Random.nextInt(1000).toString().padStart(3, '0')
            conta = "${Random.nextInt(100000).toString().padStart(5, '0')}-${Random.nextInt(10)}"
        } while (usuarioRepository.existsByContaAndAgencia(conta, agencia))

        usuario.agencia = agencia
        usuario.conta = conta
    }

    private fun injetarSegmento(usuario: Usuario) {
        strategys.forEach {
                s -> s.injetarSegmento(usuario)
        }
    }

    fun obterTodosOsUsuarios(pageable: Pageable): Page<ObterUsuarioDTO> {
        val usuarios: Page<Usuario> = usuarioRepository.findAll(pageable)

        val usuariosDTO: Page<ObterUsuarioDTO> = usuarios.map{
                u -> usuarioMapperImpl.entidadeParaObterUsuario(u)
        }

        return usuariosDTO
    }

    fun obterUsuarioPorId(id: String): ObterUsuarioDetalhadoDTO {
        val usuario = usuarioRepository.findById(id).orElseThrow({NotFoundException("Usuário não encontrado!")})
        return usuarioMapperImpl.entidadeParaObterUsuarioDetalhado(usuario)
    }

    @Transactional
    fun atualizarUsuario(dto: AtualizarUsuarioDTO): ObterUsuarioDetalhadoDTO {
        var usuario = usuarioRepository.findById(dto.id).orElseThrow({NotFoundException("Usuário não encontrado!")})

        usuario.nome = dto.nome
        usuario.senha = dto.senha
        usuario.genero = dto.genero
        usuario.rendaMensal = dto.rendaMensal

        injetarSegmento(usuario)

        return usuarioMapperImpl.entidadeParaObterUsuarioDetalhado(usuario)
    }

}