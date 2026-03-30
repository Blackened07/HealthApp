package ru.HealthApp.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.HealthApp.dto.DoctorResponseDTO;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.repository.DoctorRepository;
import ru.HealthApp.repository.entities.Account;
import ru.HealthApp.repository.entities.Doctor;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;
import ru.HealthApp.utils.PasswordUtil;
import ru.HealthApp.web.AuthController;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final HealthRecordMapper mapper;

    public Doctor findById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> ResourceNotFoundException.doctorNotFound(doctorId)
        );
    }

    public DoctorResponseDTO createDoctor(String email, String password, String firstName) {
        Doctor doctor = new Doctor();
        doctor.setEmail(email);
        doctor.setPassword(PasswordUtil.encode(password));
        doctor.setFirstName(firstName);

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapper.toResponse(savedDoctor);
    }

}
