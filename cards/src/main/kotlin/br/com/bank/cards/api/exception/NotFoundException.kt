package br.com.bank.users.api.exception

import java.lang.RuntimeException

class NotFoundException(message: String?) : RuntimeException(message) {
}