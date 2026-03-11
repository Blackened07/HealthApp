package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.HealthApp.repository.entities.Family;
import ru.HealthApp.repository.entities.FamilyRole;
import ru.HealthApp.repository.entities.User;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    /**
     * Найти семью по имени
     */
    Optional<Family> findByName(String name);

    /**
     * Проверить существование семьи по имени
     */
    boolean existsByName(String name);

    /**
     * Найти семью по пользователю
     */
    Optional<Family> findByUsers(User user);

    /**
     * Найти семью по ID пользователя
     */
    Optional<Family> findByUsersId(Long userId);
}
