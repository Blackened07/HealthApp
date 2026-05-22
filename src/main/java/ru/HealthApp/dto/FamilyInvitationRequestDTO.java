package ru.HealthApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FamilyInvitationRequestDTO(
        @NotNull
        @Email(message = "Некорректный формат email")
        String invitedUserEmail,
        @NotBlank(message = "Название семьи не может быть пустым")
        @Size(min = 2, max = 20, message = "Им должно быть от 2 до 20 символов")
        String familyName
) {
}
