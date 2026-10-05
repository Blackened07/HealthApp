package ru.HealthApp.dto.auth;

public record AuthResponseDTO(String message, Long userId, String firstName, boolean success) {
}
