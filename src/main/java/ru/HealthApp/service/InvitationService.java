package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.HealthApp.repository.InvitationRepository;
import ru.HealthApp.entities.Invitation;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;

    public Invitation findInvitationBySecretCodeAndInvitedEmailAndActorEmail(String secretCode, String invitedUserEmail, String actorEmail) {
        return invitationRepository.findInvitationBySecretCodeAndInvitedUserEmailAndActorEmail(secretCode, invitedUserEmail, actorEmail)
                .orElseThrow(() -> ResourceNotFoundException.invitationNotFound(invitedUserEmail));
    }

    public boolean isInvitationExist(String actorEmail) {
        return invitationRepository.existsByActorEmail(actorEmail);
    }

    public boolean isUserFamilyMember(Long userId) {
        User actor = userService.findById(userId);
        return !actor.isNoFamily();
    }

    public boolean isUserFamilyMember(String userEmail) {
        User user = userService.findByEmail(userEmail);
        return !user.isNoFamily();
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


    public String generateSecretCode() {

        String prefix = "FAM_";

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(prefix);
        Random rand = new Random();

        for (int i = 0; i < 9; i++) {
            code.append(chars.charAt(rand.nextInt(chars.length())));

            if (i % 3 == 0) {
                code.append("_");
            }
        }

        return code.toString().toUpperCase();

    }
}
