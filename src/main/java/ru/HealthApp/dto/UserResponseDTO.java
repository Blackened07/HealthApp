package ru.HealthApp.dto;

import ru.HealthApp.repository.entities.FamilyRole;

import java.time.LocalDateTime;
//TODO : БЕЗСМЫСЛЕННЫЙ БОЛЬШОЙ ДТО. НА СЕРВЕР ПРИ РЕГИСТРАЦИИ ДОСТАТОЧНО ОТПРАВИТЬ 200 ОК
public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        FamilyRole familyRole,
        LocalDateTime lastActivity
) implements AccountResponseDTO{
}
