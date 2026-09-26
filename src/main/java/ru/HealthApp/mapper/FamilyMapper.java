package ru.HealthApp.mapper;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.entities.Family;

@Component
public class FamilyMapper {

    public FamilyResponseDTO toResponse(Family family, String familyRole) {
        return new FamilyResponseDTO(
                family.getId(),
                family.getName(),
                familyRole
        );
    }
}
