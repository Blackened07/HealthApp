package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.mapper.HealthAppMapper;
import ru.HealthApp.repository.AccountRepository;
import ru.HealthApp.repository.FamilyRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.entities.*;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.exceptions.IllegalActionException;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.service.validators.AccessGuard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {
    private final AccountRepository accountRepository;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final UserService userService;
    private final AccessGuard accessGuard;
    private final HealthAppMapper mapper;


    @Transactional
    public FamilyResponseDTO createFamily(String adminEmail, String secondMemberEmail, String familyName) {

        User admin = userService.findByEmail(adminEmail);
        User member = userService.findByEmail(secondMemberEmail);

        if (admin.getRole() != Account.SystemRole.USER && member.getRole() != Account.SystemRole.USER) {
            throw new IllegalArgumentException();
        }

        if (!member.isNoFamily()) {
            throw new IllegalArgumentException(ExceptionMessage.USER_ALREADY_IN_FAMILY.getMessage());
        }

        if (admin.getId().equals(member.getId())) {
            throw new IllegalArgumentException("Нельзя добавить самого себя в семью");
        }

        Family family = new Family();
        family.setName(familyName);
        family = familyRepository.save(family);

        admin.setFamilyRole(FamilyRole.ADMIN);
        member.setFamilyRole(FamilyRole.MEMBER);

        family.addUser(admin);
        family.addUser(member);

        userRepository.save(admin);
        userRepository.save(member);
        familyRepository.save(family);

        return mapper.toResponse(family, member.getFamilyRole().name());
    }

    public Family findByUserId(Long userId) {
        return familyRepository.findByUsersId(userId)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);
    }

    public FamilyResponseDTO getFamilyDtoByName(String familyName) {
         Family family = familyRepository.findByName(familyName)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);

         return mapper.toResponse(
                 family,
                 FamilyRole.MEMBER.name());
    }

    private User getUserOrElseThrow(Optional<Account> unknownAccount) {

        if (unknownAccount.isEmpty()) {
            throw new IllegalArgumentException("пользователя с таким адресом не существует!");
        }

        Account account = unknownAccount.get();
        String role = account.getRole().toString();

        switch (role) {
            case "USER" -> {
                return (User) account;
            }
            case "DOCTOR" -> {
                throw new IllegalActionException("Доктор не может создать семью");
            }
        }
        return null;
    }

    @Transactional
    public void inviteToFamily(String familyName, String email) {
        Family family = familyRepository.findByName(familyName)
                .orElseThrow(ResourceNotFoundException::usersFamilyNotFound);

        Account user = accountService.findByEmail(email);
        String role = user.getRole().toString();

        switch (role) {
            case "USER" -> {
                User u = (User) user;
                if (!u.isNoFamily()) {
                    throw new IllegalArgumentException("Пользователь уже состоит в семье");
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
        // TODO: Отправить уведомление пользователю
    }


    @Transactional
    public UserResponseDTO createVirtualMember(Long familyId, String firstName) {

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

    public List<UserResponseDTO> getFamilyMembers(Long familyId, User user) {

        accessGuard.checkReadAccess(user, user);

        Family family =  familyRepository.findById(familyId)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyId));

        return family.getUsers()
                .stream()
                .map(mapper::toResponse)
                .toList();

    }

    @Transactional
    public void removeMemberFromFamily(Long familyId, Long userId) {
        User user = userService.findById(userId);
        Family family = findFamilyById(familyId);

        if (user.getFamily().getId() != familyId) {
            throw new IllegalArgumentException("Пользователь не состоит в указанной семье");
        }

        if (user.isAdmin()) {
            throw new IllegalArgumentException("Нельзя удалить админа семьи");
        }

        family.removeUser(user);
        user.setFamily(null);
        userRepository.save(user);
    }


    public Family findFamilyById(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyId));
    }

    public Family findFamilyByName(String familyName) {
        return familyRepository.findByName(familyName)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyName));
    }
}
