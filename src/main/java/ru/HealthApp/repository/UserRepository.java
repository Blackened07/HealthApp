package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.HealthApp.entities.Family;
import ru.HealthApp.entities.FamilyRole;
import ru.HealthApp.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByFamily(Family family);

    Optional<User> findByFamilyAndFamilyRole(Family family, FamilyRole role);

    List<User> findAllByLastActivityBefore(LocalDateTime threshold);

    List<User> findByFamilyId(Long familyId);


}
