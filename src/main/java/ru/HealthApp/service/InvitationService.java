package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.HealthApp.repository.InvitationRepository;
import ru.HealthApp.repository.entities.Invitation;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;

    public Invitation findInvitationBySecretCodeAndInvitedEmail(String secretCode, String email) {
        return invitationRepository.findInvitationBySecretCodeAndInvitedUserEmail(secretCode, email)
                .orElseThrow(() -> ResourceNotFoundException.invitationNotFound(email));
    }

    public boolean isUserFamilyMember(Long userId) {
        User actor = userService.findById(userId);
        return actor.isNoFamily();
    }

    public boolean isUserFamilyMember(String userEmail) {
        User user = userService.findByEmail(userEmail);
        return user.isNoFamily();
    }

    public void create(String secretCode, String email, String familyName, String actorEmail) {
        Invitation inv = new Invitation();

        LocalDateTime creationTime = LocalDateTime.now();
        LocalDateTime expTime = creationTime.plusDays(1);

        inv.setActorEmail(actorEmail);
        inv.setSecretCode(secretCode);
        inv.setInvitedUserEmail(email);
        inv.setFamilyName(familyName);
        inv.setCreatedAtTimestamp(creationTime);
        inv.setExpirationTimestamp(expTime);
        inv.setStatus("PENDING");

        invitationRepository.save(inv);

    }



}
