package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.entities.*;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.exceptions.IllegalActionException;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.mapper.FamilyMapper;
import ru.HealthApp.repository.FamilyRepository;
import ru.HealthApp.repository.InvitationRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.service.validators.FamilyActionGuardInterface;

@Service
@RequiredArgsConstructor
public class FamilyCreationService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final AccountLookupService accountLookupService;
    private final UserService userService;
    private final FamilyActionGuardInterface familyActionGuard;
    private final FamilyMapper familyMapper;

    @Transactional
    public FamilyResponseDTO getFamilyResponseDtoForNewFamily(
            String invitedUserEmail,
            String adminEmail,
            String secretCode) {

        User actor = userService.findByEmail(adminEmail);
        User invitedUser = userService.findByEmail(invitedUserEmail);

        Invitation inv = findInvitationBySecretCodeAndInvitedEmailAndActorEmail(
                secretCode,
                invitedUserEmail,
                adminEmail);

        familyActionGuard.checkInvitationToNewFamily(actor, invitedUser);

        return createFamily(
                adminEmail,
                invitedUserEmail,
                inv.getFamilyName()
        );
    }

    @Transactional(readOnly = true)
    public FamilyResponseDTO getFamilyResponseDtoForExistedFamily(
            String invitedUserEmail,
            String adminEmail,
            String secretCode
    ) {
        User admin = userService.findByEmail(adminEmail);

        familyActionGuard.checkIsAdminEmailInInvitationRequest(admin);

        Invitation invitation = findInvitationBySecretCodeAndInvitedEmailAndActorEmail(
                secretCode,
                invitedUserEmail,
                adminEmail
        );

        String familyName = invitation.getFamilyName();

        inviteToFamily(familyName, invitation.getInvitedUserEmail());

        return getFamilyDtoByName(familyName);
    }

    @Transactional
    private FamilyResponseDTO createFamily(String adminEmail, String secondMemberEmail, String familyName) {
        User admin = userService.findByEmail(adminEmail);
        User member = userService.findByEmail(secondMemberEmail);

        Family family = new Family();
        family.setName(familyName);

        admin.setFamilyRole(FamilyRole.ADMIN);
        member.setFamilyRole(FamilyRole.MEMBER);

        family.addUser(admin);
        family.addUser(member);

        userRepository.save(admin);
        userRepository.save(member);
        familyRepository.save(family);

        return familyMapper.toResponse(family, member.getFamilyRole().name());
    }

    @Transactional
    private void inviteToFamily(String familyName, String email) {
        Family family = familyRepository.findByName(familyName)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);

        Account user = accountLookupService.findByEmail(email);
        String role = user.getRole().toString();

        switch (role) {
            case "USER" -> {
                User u = (User) user;
                if (!u.isNoFamily()) {
                    throw new IllegalActionException(ExceptionMessage.USER_ALREADY_IN_FAMILY.getMessage());
                }

                family.addUser(u);
                u.setFamilyRole(FamilyRole.MEMBER);
                userRepository.save(u);
            }
            case "DOCTOR" -> {
                Doctor d = (Doctor) user;
                if (!family.isFamilyDoctor(d)) {
                    family.addDoctor(d);
                } else {
                    throw new IllegalArgumentException("Доктор уже курирует вашу семью");
                }
            }
        }
    }

    private FamilyResponseDTO getFamilyDtoByName(String familyName) {
        Family family = familyRepository.findByName(familyName)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);

        return familyMapper.toResponse(
                family,
                FamilyRole.MEMBER.name());
    }

    private Invitation findInvitationBySecretCodeAndInvitedEmailAndActorEmail(String secretCode, String invitedUserEmail, String actorEmail) {
        return invitationRepository.findInvitationBySecretCodeAndInvitedUserEmailAndActorEmail(secretCode, invitedUserEmail, actorEmail)
                .orElseThrow(() -> ResourceNotFoundException.invitationNotFound(invitedUserEmail));
    }
}
