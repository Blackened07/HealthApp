package ru.HealthApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccessDeniedException.class,
            ResourceNotFoundException.class,
            InvalidMetricException.class,
            IllegalActionException.class
    })
    public ResponseEntity<ErrorResponse> handleHealthAppException(HealthAppException ex) {

        HttpStatus status = switch (ex) {
            case AccessDeniedException ignored -> HttpStatus.FORBIDDEN;
            case ResourceNotFoundException ignored -> HttpStatus.NOT_FOUND;
            case InvalidMetricException ignored -> HttpStatus.BAD_REQUEST;
            case IllegalActionException ignored -> HttpStatus.BAD_REQUEST;
        };

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                ex.getMessage(),
                ex.isCritical()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации: " + errors,
                false
        );

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Внутренняя ошибка сервера: " + ex.getMessage(),
                true
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    public record ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String message,
            boolean critical
    ) {}
}
