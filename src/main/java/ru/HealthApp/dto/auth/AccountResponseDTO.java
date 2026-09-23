package ru.HealthApp.dto.auth;

public sealed interface AccountResponseDTO permits UserResponseDTO, DoctorResponseDTO {
}
