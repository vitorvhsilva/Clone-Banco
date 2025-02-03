package br.com.bank.users.domain.utils.mappers

import br.com.bank.users.api.dto.events.EnderecoViaCep
import br.com.bank.users.domain.entity.Endereco
import org.springframework.stereotype.Component

@Component
class EnderecoMapperImpl: EnderecoMapper {
    override fun enderecoParaEntidade(enderecoViaCep: EnderecoViaCep): Endereco {
        return Endereco (
            cep = enderecoViaCep.cep,
            logradouro = enderecoViaCep.logradouro,
            bairro = enderecoViaCep.bairro,
            uf = enderecoViaCep.uf,
            estado = enderecoViaCep.estado,
            regiao = enderecoViaCep.regiao
        )
    }
}