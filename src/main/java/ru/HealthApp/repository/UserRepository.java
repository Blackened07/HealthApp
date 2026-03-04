package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.HealthApp.repository.entities.Family;
import ru.HealthApp.repository.entities.FamilyRole;
import ru.HealthApp.repository.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);

    List<User> findAllByFamily(Family family);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByPassword(String password);

    boolean existsByEmail(String email);

    Optional<User> findByFamilyAndFamilyRole(Family family, FamilyRole role);

    List<User> findAllByLastActivityBefore(LocalDateTime threshold);
}
