package ru.HealthApp.dto;

import ru.HealthApp.dto.auth.AccountResponseDTO;

public record DoctorResponseDTO(
        Long id,
        String email,
        String firstName) implements AccountResponseDTO {
}
