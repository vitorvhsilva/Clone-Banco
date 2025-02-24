package br.com.bank.users.api.exception

import java.lang.RuntimeException

class CepInvalidoException(message: String?) : RuntimeException(message) {
}