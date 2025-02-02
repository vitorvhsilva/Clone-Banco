package br.com.bank.users.domain.service

import br.com.bank.users.api.dto.events.CartaoOutputDTO
import br.com.bank.users.api.dto.events.CatalogoCartaoOutputDTO
import br.com.bank.users.api.dto.events.PedidoCartaoCompletoDTO
import br.com.bank.users.api.dto.events.PedidoCartaoDTO
import br.com.bank.users.api.dto.input.AtualizarUsuarioDTO
import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.http.CartoesClient
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.service.strategy.SegmentoStrategy
import br.com.bank.users.domain.utils.mappers.UsuarioMapper
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UsuarioService (
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapperImpl: UsuarioMapper,
    private val strategys: List<SegmentoStrategy>,
    private val cartoesClient: CartoesClient,
    private val kafkaTemplate: KafkaTemplate<String, PedidoCartaoCompletoDTO>,
    private val logger: Logger
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
        val usuarioDetalhado = usuarioMapperImpl.entidadeParaObterUsuarioDetalhado(usuario)

        usuarioDetalhado.cartoes = obterCartoesDoUsuario(usuarioDetalhado.id!!)
        return usuarioDetalhado
    }

    @Transactional
    fun atualizarUsuario(dto: AtualizarUsuarioDTO): ObterUsuarioDetalhadoDTO {
        val usuario = usuarioRepository.findById(dto.id).orElseThrow({NotFoundException("Usuário não encontrado!")})

        usuario.nome = dto.nome
        usuario.senha = dto.senha
        usuario.genero = dto.genero
        usuario.rendaMensal = dto.rendaMensal

        injetarSegmento(usuario)

        return usuarioMapperImpl.entidadeParaObterUsuarioDetalhado(usuario)
    }

    fun obterCartoesDisponiveisParaUsuario(id: String): List<CatalogoCartaoOutputDTO> {
        val usuario = usuarioRepository.findById(id).orElseThrow({NotFoundException("Usuário não encontrado!")})

        val cartoes = cartoesClient.obterCartoesDisponiveisParaUsuario(usuario.segmento!!)
        return cartoes
    }

    fun obterCartoesDoUsuario(id: String): List<CartaoOutputDTO> {
        val cartoes = cartoesClient.obterCartoesDisponiveisDoUsuario(id)
        return cartoes
    }

    fun fazerPedidoDeCartao(dto: PedidoCartaoDTO) {
        val usuario = usuarioRepository.findById(dto.idUsuario).orElseThrow({NotFoundException("Usuário não encontrado!")})

        val cartoesEncontrados = obterCartoesDisponiveisParaUsuario(dto.idUsuario)

        if (cartoesEncontrados.none{it.id == dto.idCartao }) { //cartao nao existe
            throw NotFoundException("Cartão não encontrado!")
        }

        val pedidoCartao = PedidoCartaoCompletoDTO(
            nomeUsuario = usuario.nome,
            segmento = usuario.segmento,
            idUsuario = dto.idUsuario,
            idCartao = dto.idCartao,
            conta = usuario.conta,
            agencia = usuario.agencia
        )

        kafkaTemplate.send("pedidos-cartoes-topic", pedidoCartao.idUsuario, pedidoCartao)
        logger.info("Pedido de cartão do usuário ${dto.idUsuario} enviado!")
    }

}