package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.HealthApp.repository.entities.Family;
import ru.HealthApp.repository.entities.FamilyRole;
import ru.HealthApp.repository.entities.User;

import java.util.Optional;
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    /*Optional<Family> findById(Long id);*/
    Optional<Family> findByName(String name);

    boolean existsByName(String name);

    Optional<Family> findByUsers(User user);

    Optional<Family> findByUsersId(Long userId);
}
