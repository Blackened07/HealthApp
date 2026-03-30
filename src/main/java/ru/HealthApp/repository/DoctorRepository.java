package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.HealthApp.repository.entities.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findById(Long aLong);
}
