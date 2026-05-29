package ru.HealthApp.dto;

public record FamilyResponseDTO(
        boolean isNoFamily,
        long familyId,
        String familyName,
        String familyRole
) {

}
