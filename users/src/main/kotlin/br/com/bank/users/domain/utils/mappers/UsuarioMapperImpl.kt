package br.com.bank.users.domain.utils.mappers

import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDetalhadoDTO
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.utils.enums.StatusUsuario
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UsuarioMapperImpl: UsuarioMapper {
    override fun cadastroInputParaEntidade(dto: CadastroUsuarioInputDTO): Usuario {
        return Usuario (
            nome = dto.nome,
            email = dto.email,
            senha = dto.senha,
            cpf = dto.cpf,
            genero = dto.genero,
            dataNascimento = dto.dataNascimento,
            dataCriacaoConta = LocalDateTime.now(),
            statusUsuario = StatusUsuario.ATIVO,
            rendaMensal = dto.rendaMensal,
            saldoContaCorrente = dto.saldoContaCorrente
        )
    }

    override fun entidadeParaCadastroOutput(usuario: Usuario): CadastroUsuarioOutputDTO {
        return CadastroUsuarioOutputDTO (
            id = usuario.id,
            nome = usuario.nome,
            email = usuario.email,
            senha = usuario.senha,
            cpf = usuario.cpf,
            genero = usuario.genero,
            agencia = usuario.agencia,
            conta = usuario.conta,
            dataNascimento = usuario.dataNascimento,
            dataCriacaoConta = usuario.dataCriacaoConta,
            statusUsuario = usuario.statusUsuario,
            rendaMensal = usuario.rendaMensal,
            segmento = usuario.segmento,
            saldoContaCorrente = usuario.saldoContaCorrente
        )
    }

    override fun entidadeParaObterUsuario(usuario: Usuario): ObterUsuarioDTO {
        return ObterUsuarioDTO (
            id = usuario.id,
            nome = usuario.nome,
            email = usuario.email,
            senha = usuario.senha,
            cpf = usuario.cpf,
            genero = usuario.genero,
            agencia = usuario.agencia,
            conta = usuario.conta,
            dataNascimento = usuario.dataNascimento,
            segmento = usuario.segmento,
            saldoContaCorrente = usuario.saldoContaCorrente
        )
    }

    override fun entidadeParaObterUsuarioDetalhado(usuario: Usuario): ObterUsuarioDetalhadoDTO {
        return ObterUsuarioDetalhadoDTO (
            id = usuario.id,
            nome = usuario.nome,
            email = usuario.email,
            senha = usuario.senha,
            cpf = usuario.cpf,
            genero = usuario.genero,
            agencia = usuario.agencia,
            conta = usuario.conta,
            dataNascimento = usuario.dataNascimento,
            dataCriacaoConta = usuario.dataCriacaoConta,
            statusUsuario = usuario.statusUsuario,
            rendaMensal = usuario.rendaMensal,
            segmento = usuario.segmento,
            saldoContaCorrente = usuario.saldoContaCorrente
        )
    }

}