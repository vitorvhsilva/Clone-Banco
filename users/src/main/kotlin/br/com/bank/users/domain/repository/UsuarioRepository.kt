package br.com.bank.users.domain.repository

import br.com.bank.users.domain.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository: JpaRepository<Usuario, String> {
    fun existsByContaAndAgencia(conta: String, agencia: String): Boolean
}