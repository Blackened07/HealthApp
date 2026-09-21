package ru.HealthApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.entities.Invitation;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    /**
        Секретный код + мэйл того, кого приглашают
     */
    Optional<Invitation> findInvitationBySecretCodeAndInvitedUserEmailAndActorEmail(String secretCode, String invitedUserEmail, String actorEmail);

    @Modifying
    @Transactional
    void deleteByExpirationTimestampBefore(LocalDateTime time);

    boolean existsByActorEmail(String actorEmail);

}
