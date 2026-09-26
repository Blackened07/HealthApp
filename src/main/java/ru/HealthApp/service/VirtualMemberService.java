package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.config.UserPrincipal;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.entities.Family;
import ru.HealthApp.entities.FamilyRole;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.mapper.UserMapper;
import ru.HealthApp.repository.FamilyRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.service.validators.AccessGuardInterface;
import ru.HealthApp.web.FamilyController;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VirtualMemberService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AccessGuardInterface accessGuard;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO createVirtualMember(
            Long familyId,
            String firstName,
            UserPrincipal user) {

        User admin = userService.findById(user.userId());
        accessGuard.checkManageAccess(admin);

        Family family = findFamilyById(familyId);

        User virtualMember = new User();
        virtualMember.setEmail(admin.getEmail() + "_virtual_" + UUID.randomUUID().toString().substring(0, 8));
        virtualMember.setPassword(UUID.randomUUID().toString());
        virtualMember.setFirstName(firstName);
        virtualMember.setFamilyRole(FamilyRole.VIRTUAL);
        virtualMember.setFamily(family);
        virtualMember.setLastActivity(LocalDateTime.now());

        User savedVirtualUser = userRepository.save(virtualMember);

        return userMapper.toResponse(savedVirtualUser);
    }

    private Family findFamilyById(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyId));
    }
}
