package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.repository.AccountRepository;
import ru.HealthApp.repository.FamilyRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.repository.entities.*;
import ru.HealthApp.service.exceptions.ExceptionMessage;
import ru.HealthApp.service.exceptions.IllegalActionException;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;
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
    private final HealthRecordMapper mapper;


    @Transactional
    public FamilyResponseDTO createFamily(Long userId, String secondMemberEmail, String familyName) {

        User admin = userService.findById(userId);
        Optional<Account> unknownAccount = accountRepository.findByEmail(secondMemberEmail);
        User member = getUserOrElseThrow(unknownAccount);

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

        return mapper.toResponse(family);
    }

    private User getUserOrElseThrow(Optional<Account> unknownAccount) {

        if (unknownAccount.isEmpty()) {
            throw new IllegalArgumentException("пользователя с таким адресом не существует!");
        } else {
            Account account = unknownAccount.get();

            switch (account) {
                case User user -> {
                    return user;
                }
                case Doctor doctor -> {
                    throw new IllegalActionException("Доктор не может создать семью");
                }
            }

        }
    }

    @Transactional
    public void inviteToFamily(Long familyId, String email) {
        Family family = findFamilyById(familyId);

        Account user = accountService.findByEmail(email);

        switch (user) {
            case User u -> {
                if (u.getFamily() != null) {
                    throw new IllegalArgumentException("Пользователь уже состоит в семье");
                }

                family.addUser(u);
                u.setFamilyRole(FamilyRole.MEMBER);
                userRepository.save(u);
            }
            case Doctor d -> {
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

    public List<UserResponseDTO> getFamilyMembers(Long familyId) {
        return null;
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
}
