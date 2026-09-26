package ru.HealthApp.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.HealthApp.dto.auth.DoctorResponseDTO;
import ru.HealthApp.entities.Account;
import ru.HealthApp.mapper.DoctorMapper;
import ru.HealthApp.repository.DoctorRepository;
import ru.HealthApp.entities.Doctor;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.utils.PasswordUtil;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper mapper;

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

    public void saveVerify(Account account) {
        Doctor doctor = (Doctor) account;
        doctor.setEnabled(true);
        doctor.setVerificationCode(null);
        doctorRepository.save(doctor);
    }
}
