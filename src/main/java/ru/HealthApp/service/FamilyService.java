package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.config.UserPrincipal;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.mapper.HealthAppMapper;
import ru.HealthApp.repository.FamilyRepository;
import ru.HealthApp.repository.InvitationRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.entities.*;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.exceptions.IllegalActionException;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.service.validators.AccessGuard;
import ru.HealthApp.service.validators.FamilyActionGuard;
import ru.HealthApp.web.FamilyController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final AccountService accountService;
    private final UserService userService;
    private final AccessGuard accessGuard;
    private final FamilyActionGuard familyActionGuard;
    private final HealthAppMapper mapper;

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

    @Transactional(readOnly = true)
    public FamilyResponseDTO getUserFamilyInfo(Long userId) {

        User user = userService.findById(userId);

        if (user.isNoFamily() || user.getFamily() == null) {
            return new FamilyResponseDTO(0L, "", "");
        }

        Family family = user.getFamily();

        return new FamilyResponseDTO(
                family.getId(),
                family.getName(),
                user.getFamilyRole().name()
        );
    }

    @Transactional
    public UserResponseDTO getVirtualDto(
            Long familyId,
            FamilyController.CreateVirtualMemberRequest request,
            UserPrincipal user) {

        User admin = userService.findById(user.userId());
        accessGuard.checkManageAccess(admin);

        return createVirtualMember(
                familyId,
                request.firstName()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getFamilyMembers(Long familyId, Long userId) {
        User user = userService.findById(userId);
        accessGuard.checkReadAccess(user, user);

        Family family = findFamilyById(familyId);

        return family.getUsers()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void removeMemberFromFamily(Long id, Long familyId, String userEmail) {
        User admin = userService.findById(id);
        accessGuard.checkManageAccess(admin);

        User deletionUser = userService.findByEmail(userEmail);
        Family family = findFamilyById(familyId);

        familyActionGuard.checkDeletion(deletionUser, familyId);

        family.removeUser(deletionUser);
        deletionUser.setFamily(null);
        userRepository.save(deletionUser);
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

        return mapper.toResponse(family, member.getFamilyRole().name());
    }

    @Transactional
    private void inviteToFamily(String familyName, String email) {
        Family family = familyRepository.findByName(familyName)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);

        Account user = accountService.findByEmail(email);
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

    @Transactional
    private UserResponseDTO createVirtualMember(Long familyId, String firstName) {

        Family family = findFamilyById(familyId);

        User admin = family.findAdmin();

        accessGuard.checkManageAccess(admin);

        User virtualMember = new User();
        virtualMember.setEmail(admin.getEmail() + "_virtual_" + UUID.randomUUID().toString().substring(0, 8));
        virtualMember.setPassword(UUID.randomUUID().toString()); // случайный пароль
        virtualMember.setFirstName(firstName);
        virtualMember.setFamilyRole(FamilyRole.VIRTUAL);
        virtualMember.setFamily(family);
        virtualMember.setLastActivity(LocalDateTime.now());

        User savedVirtualUser = userRepository.save(virtualMember);

        return mapper.toResponse(savedVirtualUser);
    }

    private Family findFamilyById(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyId));
    }

    private Family findFamilyByName(String familyName) {
        return familyRepository.findByName(familyName)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyName));
    }

    private Family findByUserId(Long userId) {
        return familyRepository.findByUsersId(userId)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);
    }

    private FamilyResponseDTO getFamilyDtoByName(String familyName) {
        Family family = familyRepository.findByName(familyName)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);

        return mapper.toResponse(
                family,
                FamilyRole.MEMBER.name());
    }

    private Invitation findInvitationBySecretCodeAndInvitedEmailAndActorEmail(String secretCode, String invitedUserEmail, String actorEmail) {
        return invitationRepository.findInvitationBySecretCodeAndInvitedUserEmailAndActorEmail(secretCode, invitedUserEmail, actorEmail)
                .orElseThrow(() -> ResourceNotFoundException.invitationNotFound(invitedUserEmail));
    }
}
