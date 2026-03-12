package ru.HealthApp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.repository.entities.Family;
import ru.HealthApp.repository.entities.FamilyRole;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.FamilyService;
import jakarta.validation.constraints.*;
import ru.HealthApp.service.UserService;
import ru.HealthApp.service.validators.AccessGuard;

import java.util.List;

@RestController
@RequestMapping("/api/v1/families")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;
    private final UserService userService;
    private final AccessGuard accessGuard;


    @PostMapping("/create")
    public ResponseEntity<Family> createFamily(@RequestBody CreateFamilyRequest request) {
        Family family = familyService.createFamily(
                request.userId(),
                request.secondMemberEmail,
                request.familyName()
        );
        return ResponseEntity.ok(family);
    }

    @PostMapping("/{familyId}/invite")
    public ResponseEntity<Void> inviteMember(
            @PathVariable Long familyId,
            @RequestBody InviteMemberRequest request,
            @RequestParam Long adminId) {
        
        User admin = userService.findById(adminId);

        accessGuard.checkManageAccess(admin);
        
        familyService.inviteToFamily(familyId, request.email(), request.role());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{familyId}/virtual-members")
    public ResponseEntity<User> createVirtualMember(
            @PathVariable Long familyId,
            @RequestBody CreateVirtualMemberRequest request,
            @RequestParam Long adminId) {
        
        User admin = userService.findById(adminId);

        accessGuard.checkManageAccess(admin);
        
        User virtualMember = familyService.createVirtualMember(
                familyId,
                request.firstName(),
                request.role()
        );
        
        return ResponseEntity.ok(virtualMember);
    }

    @GetMapping("/{familyId}/members")
    public ResponseEntity<List<User>> getFamilyMembers(
            @PathVariable Long familyId,
            @RequestParam Long currentUserId) {
        
        User admin = userService.findById(currentUserId);
        
        accessGuard.checkFamilyDashboardAccess(admin);
        
        List<User> members = familyService.getFamilyMembers(familyId);
        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/{familyId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long familyId,
            @PathVariable Long userId,
            @RequestParam Long adminId) {
        
        User admin = userService.findById(adminId);
        
        accessGuard.checkManageAccess(admin);
        
        familyService.removeMemberFromFamily(familyId, userId);
        return ResponseEntity.noContent().build();
    }


    public record CreateFamilyRequest(
            @NotNull(message = "ID пользователя обязателен")
            @Positive(message = "ID пользователя должен быть положительным")
            Long userId,
            
            @NotBlank(message = "Email второго члена обязателен")
            @Email(message = "Некорректный формат email")
            String secondMemberEmail,
            
            @NotBlank(message = "Название семьи не может быть пустым")
            @Size(min = 2, max = 20, message = "Название семьи от 2 до 20 символов")
            String familyName
    ) {}

    public record InviteMemberRequest(
            @NotBlank(message = "Email не может быть пустым")
            @Email(message = "Некорректный формат email")
            String email,
            
            @NotNull(message = "Роль обязательна")
            FamilyRole role
    ) {}

    public record CreateVirtualMemberRequest(
            @NotBlank(message = "Имя не может быть пустым")
            @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
            String firstName,
            
            @NotNull(message = "Роль обязательна")
            FamilyRole role
    ) {}
}
