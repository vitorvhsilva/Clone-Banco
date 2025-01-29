package br.com.bank.users.domain.service.strategy

import br.com.bank.users.domain.entity.Usuario

interface SegmentoStrategy {
    fun injetarSegmento(usuario: Usuario)
}