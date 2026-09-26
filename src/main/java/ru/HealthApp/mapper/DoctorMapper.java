package ru.HealthApp.mapper;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.auth.DoctorResponseDTO;
import ru.HealthApp.entities.Doctor;

@Component
public class DoctorMapper {

    public DoctorResponseDTO toResponse(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getFirstName()
        );
    }
}
