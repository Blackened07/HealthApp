package ru.HealthApp.dto.auth;

import ru.HealthApp.dto.DoctorResponseDTO;
import ru.HealthApp.dto.UserResponseDTO;

public sealed interface AccountResponseDTO permits UserResponseDTO, DoctorResponseDTO {
}
