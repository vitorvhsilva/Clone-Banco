package br.com.bank.users.domain.repository

import br.com.bank.users.domain.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository: JpaRepository<Usuario, String> {
    fun existsByContaAndAgencia(conta: String, agencia: String): Boolean
    fun findByEmail(email: String): MutableList<Usuario>
    fun existsByEmailOrCpf(email: String, cpf: String): Boolean
    fun findByEmailOrCpf(email: String, cpf: String): MutableList<Usuario>
}