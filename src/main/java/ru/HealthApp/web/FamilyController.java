package ru.HealthApp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.repository.entities.Family;
import ru.HealthApp.repository.entities.Invitation;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.security.UserPrincipal;
import ru.HealthApp.service.FamilyService;
import jakarta.validation.constraints.*;
import ru.HealthApp.service.InvitationService;
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
    private final InvitationService invitationService;


    @PostMapping("/{adminEmail}")
    public ResponseEntity<FamilyResponseDTO> createFamily(
            @AuthenticationPrincipal UserPrincipal member,
            @RequestBody CreateFamilyRequest request) {

        String invitedUSerEmail = member.email();
        String adminEmail = request.adminEmail;
        String secretCode = request.secretCode;

        Invitation inv = invitationService.findInvitationBySecretCodeAndInvitedEmailAndActorEmail(
                secretCode,
                invitedUSerEmail,
                adminEmail);


        FamilyResponseDTO family = familyService.createFamily(
                adminEmail,
                invitedUSerEmail,
                inv.getFamilyName()
        );
        return ResponseEntity.ok(family);
    }

    @GetMapping("/is-no-family")
    public ResponseEntity<FamilyResponseDTO> isFamilyUser(@AuthenticationPrincipal UserPrincipal actor) {
        Long id = actor.userId();

        User user = userService.findById(id);

        FamilyResponseDTO request;

        if (user.isNoFamily()) {
            request = new FamilyResponseDTO(true, 0, "", "");
        } else {
            Family family = familyService.findByUserId(id);
            request = new FamilyResponseDTO(
                    false,
                    family.getId(),
                    family.getName(),
                    user.getFamilyRole().name()
            );
        }

        return ResponseEntity.ok(request);
    }


    @PostMapping("/{familyId}/invite")
    public ResponseEntity<Void> inviteMember(
            @PathVariable Long familyId,
            @RequestBody InviteMemberRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        User admin = userService.findById(user.userId());

        accessGuard.checkManageAccess(admin);
        
        familyService.inviteToFamily(familyId, request.email());
        return ResponseEntity.ok().build();
    }

    //invite doctor to family

    @PostMapping("/{familyId}/virtual-members")
    public ResponseEntity<UserResponseDTO> createVirtualMember(
            @PathVariable Long familyId,
            @RequestBody CreateVirtualMemberRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        User admin = userService.findById(user.userId());

        accessGuard.checkManageAccess(admin);
        
        UserResponseDTO virtualMember = familyService.createVirtualMember(
                familyId,
                request.firstName()
        );
        
        return ResponseEntity.ok(virtualMember);
    }

    @GetMapping("/{familyId}/members")
    public ResponseEntity<List<UserResponseDTO>> getFamilyMembers(
            @PathVariable Long familyId,
            @AuthenticationPrincipal UserPrincipal user) {

        User u = userService.findById(user.userId());
        
        List<UserResponseDTO> members = familyService.getFamilyMembers(familyId, u);
        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/{familyId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long familyId,
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal user) {
        
        User admin = userService.findById(user.userId());
        
        accessGuard.checkManageAccess(admin);
        
        familyService.removeMemberFromFamily(familyId, userId);
        return ResponseEntity.noContent().build();
    }


    public record CreateFamilyRequest(
            @NotNull
            String secretCode,
            
            @NotBlank(message = "Email обязателен")
            @Email(message = "Некорректный формат email")
            String adminEmail

    ) {}

    public record InviteMemberRequest(
            @NotBlank(message = "Email не может быть пустым")
            @Email(message = "Некорректный формат email")
            String email
            
           /* @NotNull(message = "Роль обязательна")
            FamilyRole role*/
    ) {}

    public record CreateVirtualMemberRequest(
            @NotBlank(message = "Имя не может быть пустым")
            @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 50 символов")
            String firstName
    ) {}


}
