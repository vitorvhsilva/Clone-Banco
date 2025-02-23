package br.com.bank.payments.api.dto.output;

import java.time.LocalDateTime;

public class ErrorDTO {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;
    private String message;

    public ErrorDTO() {
    }

    public ErrorDTO(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
