package br.com.bank.users.domain.service

import br.com.bank.users.api.dto.events.EnderecoViaCep
import br.com.bank.users.api.dto.events.PagarFaturaEventDTO
import br.com.bank.users.api.dto.events.PedidoCartaoCompletoDTO
import br.com.bank.users.api.dto.input.PedidoCartaoInputDTO
import br.com.bank.users.api.dto.input.AtualizarUsuarioDTO
import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.input.PagarFaturaInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.http.ViaCepClient
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.service.strategy.SegmentoStrategy
import br.com.bank.users.domain.utils.enums.StatusTransacao
import br.com.bank.users.domain.utils.mappers.EnderecoMapper
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
    private val enderecoMapperImpl: EnderecoMapper,
    private val viaCepClient: ViaCepClient,
    private val segmentoStrategys: List<SegmentoStrategy>,
    private val pedidoCartaoKafkaTemplate: KafkaTemplate<String, PedidoCartaoCompletoDTO>,
    private val pagarFaturaKafkaTemplate: KafkaTemplate<String, PagarFaturaEventDTO>,
    private val logger: Logger
) {
    fun cadastrarUsuario(dto: CadastroUsuarioInputDTO): ResponseEntity<CadastroUsuarioOutputDTO> {
        var usuario: Usuario = usuarioMapperImpl.cadastroInputParaEntidade(dto)

        injetarAgenciaeConta(usuario)
        injetarSegmento(usuario)
        injetarEndereco(usuario, dto.cep)

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
        segmentoStrategys.forEach {
                s -> s.injetarSegmento(usuario)
        }
    }

    private fun injetarEndereco(usuario: Usuario, cep: String) {
        var enderecoViaCep: EnderecoViaCep? = null
        try {
            enderecoViaCep = viaCepClient.obterEnderecoDoUsuario(cep)
        } catch (e: Exception) {
            logger.error("Erro ao obter endereço: ${e.message}")
        }

        usuario.endereco = enderecoMapperImpl.enderecoParaEntidade(enderecoViaCep!!)
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
        val usuario = usuarioRepository.findById(dto.id).orElseThrow({NotFoundException("Usuário não encontrado!")})

        usuario.nome = dto.nome
        usuario.senha = dto.senha
        usuario.genero = dto.genero
        usuario.rendaMensal = dto.rendaMensal

        injetarSegmento(usuario)

        return usuarioMapperImpl.entidadeParaObterUsuarioDetalhado(usuario)
    }

    @Transactional
    fun usuarioReceberSalario(id: String): ObterUsuarioDTO {
        val usuario = usuarioRepository.findById(id).orElseThrow({NotFoundException("Usuário não encontrado!")})

        usuario.saldoContaCorrente += usuario.rendaMensal

        return usuarioMapperImpl.entidadeParaObterUsuario(usuario)
    }

    fun fazerPedidoDeCartao(dto: PedidoCartaoInputDTO): ResponseEntity<Void> {
        val usuario = usuarioRepository.findById(dto.idUsuario).orElseThrow({NotFoundException("Usuário não encontrado!")})

        val pedidoCartao = PedidoCartaoCompletoDTO(
            nomeUsuario = usuario.nome,
            segmento = usuario.segmento,
            idUsuario = dto.idUsuario,
            idCartao = dto.idCartao,
            conta = usuario.conta,
            agencia = usuario.agencia
        )

        pedidoCartaoKafkaTemplate.send("pedido-cartoes-topic", pedidoCartao.idUsuario, pedidoCartao)
        logger.info("Pedido de cartão do usuário ${dto.idUsuario} enviado!")

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    fun pagarFaturaDoCartao(dto: PagarFaturaInputDTO): PagarFaturaEventDTO {
        val usuario = usuarioRepository.findById(dto.idUsuario).orElseThrow({NotFoundException("Usuário não encontrado!")})

        val pagarFatura = PagarFaturaEventDTO(
            idUsuario = dto.idUsuario,
            idCartao = dto.idCartao,
            valorFatura = dto.valorFatura,
            mesAnoFatura = dto.mesAnoFatura,
            status = StatusTransacao.EM_PROCESSAMENTO,
            mensagem = ""
        )

        usuario.diminuirSaldo(pagarFatura.valorFatura)

        pagarFaturaKafkaTemplate.send("pagar-fatura-topic", pagarFatura.idUsuario, pagarFatura)
        logger.info("Pedido de pagamento de fatura do usuário ${dto.idUsuario} enviado!")

        return pagarFatura
    }

}