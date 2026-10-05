package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.entities.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.enabled = false AND a.verificationExpiresAt < :threshold")
    List<Account> findUnverifiedAccountsOlderThan(LocalDateTime threshold);

    @Transactional
    @Modifying
    @Query("DELETE FROM Account a WHERE a.enabled = false AND a.verificationExpiresAt < :threshold")
    void deleteUnverifiedAccountsOlderThan(LocalDateTime threshold);

}
