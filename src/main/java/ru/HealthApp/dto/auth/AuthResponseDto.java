package ru.HealthApp.dto.auth;

public record AuthResponseDto(String message, Long userId, String firstName, boolean success) {
}
