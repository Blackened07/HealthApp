package ru.HealthApp.dto.auth;

import jakarta.validation.constraints.*;
import ru.HealthApp.entities.Account;

public record RegisterAccountRequestDto(
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$",
                message = "Пароль должен содержать буквы и цифры")
        String password,

        @NotBlank(message = "Имя не может быть пустым")
        @Size(min = 2, max = 20, message = "Им должно быть от 2 до 20 символов")
        String firstName,

        @NotNull(message = "Не выбрана роль в приложении")
        Account.SystemRole systemRole
) {
}
