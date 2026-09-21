package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.HealthApp.entities.Account;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /*Optional<Account> findById(long id);*/

    Optional<Account> findByEmail(String email);

    Optional<Account> findUserByPassword(String password);

    boolean existsByEmail(String email);

}
