package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.HealthApp.repository.entities.Invitation;

import java.util.Optional;
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    /**
        Секретный код + мэйл того, кого приглашают
     */
    Optional<Invitation> findInvitationBySecretCodeAndInvitedUserEmail(String secretCode, String email);

}
