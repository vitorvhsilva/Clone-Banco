package br.com.bank.users.domain.entity

import jakarta.persistence.Embeddable

@Embeddable
data class Endereco(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val uf: String,
    val estado: String,
    val regiao: String
)
