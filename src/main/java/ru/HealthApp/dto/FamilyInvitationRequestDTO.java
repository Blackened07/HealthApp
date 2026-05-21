package ru.HealthApp.dto;

import jakarta.validation.constraints.NotNull;

public record FamilyInvitationRequestDTO(
        @NotNull
        String invitedUserEmail,
        @NotNull
        String familyName
) {
}
