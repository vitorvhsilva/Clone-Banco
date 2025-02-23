package br.com.bank.payments.api.exception;

import br.com.bank.payments.api.dto.output.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorDTO> handleException(Exception e) {
        ErrorDTO error = new ErrorDTO(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ErrorDTO> handleNotFoundException(Exception e) {
        ErrorDTO error = new ErrorDTO(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
