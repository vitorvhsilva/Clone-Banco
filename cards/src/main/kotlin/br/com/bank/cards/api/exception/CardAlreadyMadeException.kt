package br.com.bank.users.api.exception

import java.lang.RuntimeException

class CardAlreadyMadeException(message: String?) : RuntimeException(message) {
}