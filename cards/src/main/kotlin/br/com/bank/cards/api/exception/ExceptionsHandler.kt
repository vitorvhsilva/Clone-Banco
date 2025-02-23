package br.com.bank.cards.api.exception

import br.com.bank.cards.api.dto.output.ErrorDTO
import br.com.bank.users.api.exception.CardAlreadyMadeException
import br.com.bank.users.api.exception.NotFoundException
import br.com.bank.users.api.exception.SegmentoNotAllowedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionsHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorDTO> {
        val error = ErrorDTO(error = HttpStatus.BAD_REQUEST.name, message = e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<ErrorDTO> {
        val errorResponse = ErrorDTO(error = HttpStatus.BAD_REQUEST.name, message = e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(LimitException::class)
    fun handleLimitException(e: LimitException): ResponseEntity<ErrorDTO> {
        val error = ErrorDTO(error = HttpStatus.BAD_REQUEST.name, message = e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(SegmentoNotAllowedException::class)
    fun handleSegmentoNotAllowedException(e: SegmentoNotAllowedException): ResponseEntity<ErrorDTO> {
        val error = ErrorDTO(error = HttpStatus.BAD_REQUEST.name, message = e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(CardAlreadyMadeException::class)
    fun handleCardAlreadyMadeException(e: CardAlreadyMadeException): ResponseEntity<ErrorDTO> {
        val error = ErrorDTO(error = HttpStatus.BAD_REQUEST.name, message = e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

}