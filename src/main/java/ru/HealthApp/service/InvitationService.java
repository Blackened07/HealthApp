package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.config.UserPrincipal;
import ru.HealthApp.dto.FamilyInvitationRequestDTO;
import ru.HealthApp.dto.FamilyInvitationResponseDto;
import ru.HealthApp.exceptions.IllegalActionException;
import ru.HealthApp.repository.InvitationRepository;
import ru.HealthApp.entities.Invitation;
import ru.HealthApp.entities.User;
import ru.HealthApp.service.validators.FamilyActionGuard;
import ru.HealthApp.utils.CodeGeneratorUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserService userService;
    private final FamilyActionGuard familyActionGuard;

    @Transactional(readOnly = true)
    public boolean isInvitationToExistFamilySuccess(String adminEmail, UserPrincipal userPrincipal) {
        User admin = userService.findByEmail(adminEmail);
        User user = userService.findById(userPrincipal.userId());
        return familyActionGuard.checkInvitationToExistFamily(admin, user);
    }

    @Transactional
    public FamilyInvitationResponseDto createInvitation(
            UserPrincipal actor,
            FamilyInvitationRequestDTO request
    ) {
        String actorEmail = actor.email();
        String invitedUserEmail = request.invitedUserEmail();
        String familyName = request.familyName();

        checkInvitationIsPossible(actorEmail, invitedUserEmail);

        String secretCode = CodeGeneratorUtil.generateFamilyInvitationCode();
        create(secretCode, invitedUserEmail, familyName, actorEmail);

        return new FamilyInvitationResponseDto(secretCode);
    }

    private void checkInvitationIsPossible(String actorEmail, String invitedUserEmail) {
        if (isInvitationExist(actorEmail, invitedUserEmail)) {
            throw IllegalActionException.getInvitationAlreadyExistException();
        }

        User newAdmin = userService.findByEmail(actorEmail);
        User invitedUser = userService.findByEmail(invitedUserEmail);

        familyActionGuard.checkInvitationToNewFamily(newAdmin, invitedUser);
    }

    private boolean isInvitationExist(String actorEmail, String invitedUserEmail) {
        return invitationRepository.existsByActorEmailAndInvitedUserEmail(actorEmail, invitedUserEmail);
    }

    private void create(String secretCode, String email, String familyName, String actorEmail) {
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
