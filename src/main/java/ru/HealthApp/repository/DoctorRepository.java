package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.HealthApp.repository.entities.Doctor;

import java.util.Optional;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findById(Long aLong);
}
