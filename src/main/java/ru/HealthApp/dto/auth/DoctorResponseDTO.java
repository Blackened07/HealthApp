package ru.HealthApp.dto.auth;

public record DoctorResponseDTO(
        Long id,
        String email,
        String firstName) implements AccountResponseDTO {
}
