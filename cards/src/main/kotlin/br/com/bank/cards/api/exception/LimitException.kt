package br.com.bank.cards.api.exception

import java.lang.RuntimeException

class LimitException(message: String?) : RuntimeException(message) {
}