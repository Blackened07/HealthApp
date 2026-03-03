package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    List<HealthRecord> findAllByUserInOrderByTimestampDesc(List<User> users);

    List<HealthRecord> findAllByUserAndTypeOrderByTimestampDesc(User user, String type);

    List<HealthRecord> findAllByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);

    /// Поиск последних записей по типу (чтобы быстро показать текущее состояние)
    Optional<HealthRecord> findFirstByUserAndTypeOrderByTimestampDesc(User user, String type);
}
