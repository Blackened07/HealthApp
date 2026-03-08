package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.HealthApp.repository.entities.HealthRecord;
import ru.HealthApp.repository.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    Optional<HealthRecord> findById(long recordID);

    List<HealthRecord> findAllByUserInOrderByTimestampDesc(List<User> users);

    List<HealthRecord> findAllByUserAndTypeOrderByTimestampDesc(User user, String type, LocalDateTime start, LocalDateTime end);

    List<HealthRecord> findAllByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);

    /// Поиск последних записей по типу (чтобы быстро показать текущее состояние)
    Optional<HealthRecord> findFirstByUserAndTypeOrderByTimestampDesc(User user, String type);

    Optional<HealthRecord> findFirstByUserOrderByTimestampDesc(User user);
}
