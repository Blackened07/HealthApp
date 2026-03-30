package ru.HealthApp.dto;

public record DoctorResponseDTO(
        Long id,
        String email,
        String firstName) implements AccountResponseDTO {
}
