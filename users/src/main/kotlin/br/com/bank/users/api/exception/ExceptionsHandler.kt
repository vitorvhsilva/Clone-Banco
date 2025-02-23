package br.com.bank.users.api.exception

import br.com.bank.users.api.dto.output.ErrorDTO
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
}