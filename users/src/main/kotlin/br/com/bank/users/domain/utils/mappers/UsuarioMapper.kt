package br.com.bank.users.domain.utils.mappers

import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.api.dto.output.ObterUsuarioDTO
import br.com.bank.users.domain.entity.Usuario

interface UsuarioMapper {
    fun cadastroInputParaEntidade(dto: CadastroUsuarioInputDTO): Usuario
    fun entidadeParaCadastroOutput(usuario: Usuario): CadastroUsuarioOutputDTO
    fun entidadeParaObterUsuario(usuario: Usuario): ObterUsuarioDTO
}