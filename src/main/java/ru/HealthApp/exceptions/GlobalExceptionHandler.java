package ru.HealthApp.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccessDeniedException.class,
            ResourceNotFoundException.class,
            InvalidMetricException.class,
            IllegalActionException.class
    })
    public ResponseEntity<ErrorResponse> handleHealthAppException(HealthAppException ex, HttpServletRequest request) {

        HttpStatus status = switch (ex) {
            case AccessDeniedException ignored -> HttpStatus.FORBIDDEN;
            case ResourceNotFoundException ignored -> HttpStatus.NOT_FOUND;
            case InvalidMetricException ignored -> HttpStatus.BAD_REQUEST;
            case IllegalActionException ignored -> HttpStatus.BAD_REQUEST;
        };

        if (ex.isCritical()) {
            log.error("КРИТИЧЕСКАЯ ОБИШКА: {} на URL: {}. IP клиента: {}",
                    ex.getMessage(), request.getRequestURI(), request.getRemoteAddr(), ex);
        } else {
            log.warn("Бизнес-ошибка ({}): {} на URL: {}. IP: {}",
                    status.value(), ex.getMessage(), request.getRequestURI(), request.getRemoteAddr());
        }

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                ex.getMessage(),
                ex.isCritical()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        log.warn("Ошибка валидации DTO на URL: {}. Неверные поля: {}. IP: {}",
                request.getRequestURI(), errors, request.getRemoteAddr());

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
