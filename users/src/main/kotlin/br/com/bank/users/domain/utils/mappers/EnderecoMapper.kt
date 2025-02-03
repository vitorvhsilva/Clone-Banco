package br.com.bank.users.domain.utils.mappers

import br.com.bank.users.api.dto.events.EnderecoViaCep
import br.com.bank.users.domain.entity.Endereco

interface EnderecoMapper {
    fun enderecoParaEntidade(enderecoViaCep: EnderecoViaCep): Endereco
}