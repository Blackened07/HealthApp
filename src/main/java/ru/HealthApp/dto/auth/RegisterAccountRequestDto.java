package ru.HealthApp.dto.auth;

import jakarta.validation.constraints.*;
import ru.HealthApp.entities.Account;

public record RegisterAccountRequestDTO(
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email,
        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
        String password,
        @NotBlank(message = "Имя не может быть пустым")
        String firstName,
        @NotNull(message = "Роль не может быть пустой")
        Account.SystemRole systemRole
) {}
