package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.HealthApp.repository.FamilyInvitationRepository;
import ru.HealthApp.repository.entities.Invitation;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private FamilyInvitationRepository familyInvitationRepository;

    public Invitation findInvitationBySecretCodeAndInvitedEmail(String secretCode, String email) {
        return familyInvitationRepository.findInvitationBySecretCodeAndInvitedEmail(secretCode, email)
                .orElseThrow(() -> ResourceNotFoundException.invitationNotFound(email));
    }

}
