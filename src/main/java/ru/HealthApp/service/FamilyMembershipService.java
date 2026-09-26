package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.entities.Family;
import ru.HealthApp.entities.FamilyRole;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.mapper.FamilyMapper;
import ru.HealthApp.mapper.UserMapper;
import ru.HealthApp.repository.FamilyRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.service.validators.AccessGuardInterface;
import ru.HealthApp.service.validators.FamilyActionGuardInterface;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyMembershipService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AccessGuardInterface accessGuard;
    private final FamilyActionGuardInterface familyActionGuard;
    private final FamilyMapper familyMapper;
    private final UserMapper userMapper;

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

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getFamilyMembers(Long familyId, Long userId) {
        User user = userService.findById(userId);
        accessGuard.checkReadAccess(user, user);

        Family family = findFamilyById(familyId);

        return family.getUsers()
                .stream()
                .map(userMapper::toResponse)
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

    private Family findFamilyById(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyId));
    }
}
