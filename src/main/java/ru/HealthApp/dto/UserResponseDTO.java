package ru.HealthApp.dto;

import ru.HealthApp.repository.entities.FamilyRole;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        FamilyRole familyRole,
        LocalDateTime lastActivity
) implements AccountResponseDTO{
}
