package br.com.bank.users.api.exception

import java.lang.RuntimeException

class SegmentoNotAllowedException(message: String?) : RuntimeException(message) {
}