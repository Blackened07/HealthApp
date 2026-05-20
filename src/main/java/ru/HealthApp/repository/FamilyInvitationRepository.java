package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.HealthApp.repository.entities.Invitation;

import java.util.Optional;

public interface FamilyInvitationRepository extends JpaRepository<Invitation, Long> {
    /**
        Секретный код + мэйл того, кого приглашают
     */
    Optional<Invitation> findInvitationBySecretCodeAndInvitedEmail(String secretCode, String email);

}
