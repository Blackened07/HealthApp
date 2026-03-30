package ru.HealthApp.dto;

public sealed interface AccountResponseDTO permits UserResponseDTO, DoctorResponseDTO {
}
