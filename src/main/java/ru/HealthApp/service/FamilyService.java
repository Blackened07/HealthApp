package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.repository.FamilyRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.repository.entities.Family;
import ru.HealthApp.repository.entities.FamilyRole;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.ExceptionMessage;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Создание новой семьи с админом
     */
    @Transactional
    public Family createFamily(Long userId, String secondMemberEmail, String familyName) {

        User admin = userService.findById(userId);
        Optional<User> user = userRepository.findByEmail(secondMemberEmail);
        User member;

        if (user.isEmpty()) {
            throw new IllegalArgumentException("пользователя с таким адресом не существует!");
        } else {
            member = user.get();
        }

        if (!member.isNoFamily()) {
            throw new IllegalArgumentException(ExceptionMessage.USER_ALREADY_IN_FAMILY.getMessage());
        }

        Family family = new Family();
        family.setName(familyName);
        family = familyRepository.save(family);

        admin.setFamily(family);
        admin.setFamilyRole(FamilyRole.ADMIN);

        member.setFamily(family);
        member.setFamilyRole(FamilyRole.MEMBER);

        userRepository.save(admin);

        return family;
    }

    /**
     * Приглашение существующего пользователя в семью
     */
    @Transactional
    public void inviteToFamily(Long familyId, String email, FamilyRole role) {
        // Находим семью
        Family family = findFamilyById(familyId);

        // Находим пользователя по email
        User user = findByEmail(email);

        // Проверяем, что пользователь еще не состоит в семье
        if (user.getFamily() != null) {
            throw new IllegalArgumentException("Пользователь уже состоит в семье");
        }

        // Добавляем пользователя в семью
        user.setFamily(family);
        user.setFamilyRole(role);
        userRepository.save(user);

        // TODO: Отправить уведомление пользователю
    }

    /**
     * Создание виртуального члена семьи
     */
    @Transactional
    public User createVirtualMember(Long familyId, String firstName, FamilyRole role) {
        // Находим семью
        Family family = findFamilyById(familyId);

        // Находим админа семьи
        User admin = family.findAdmin();

        // Создаем виртуального пользователя
        User virtualMember = new User();
        virtualMember.setEmail(admin.getEmail() + "_virtual_" + UUID.randomUUID().toString().substring(0, 8));
        virtualMember.setPassword(UUID.randomUUID().toString()); // случайный пароль
        virtualMember.setFirstName(firstName);
        virtualMember.setFamilyRole(FamilyRole.VIRTUAL);
        virtualMember.setFamily(family);
        virtualMember.setLastActivity(LocalDateTime.now());

        return userRepository.save(virtualMember);
    }

    /**
     * Получение всех членов семьи
     */
    public List<User> getFamilyMembers(Long familyId) {
        return userRepository.findByFamilyId(familyId);
    }

    /**
     * Удаление члена из семьи
     */
    @Transactional
    public void removeMemberFromFamily(Long familyId, Long userId) {
        User user = findUserById(userId);
        Family family = findFamilyById(familyId);

        if (user.getFamily().getId() != familyId) {
            throw new IllegalArgumentException("Пользователь не состоит в указанной семье");
        }

        // Нельзя удалить админа семьи
        if (user.isAdmin()) {
            throw new IllegalArgumentException("Нельзя удалить админа семьи");
        }

        user.setFamily(null);
        userRepository.save(user);
    }

    /**
     * Поиск семьи по ID
     */
    public Family findFamilyById(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> ResourceNotFoundException.familyNotFound(familyId));
    }

    /**
     * Поиск пользователя по ID
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(userId));
    }

    /**
     * Поиск пользователя по email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    /**
     * Проверка существования email
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
