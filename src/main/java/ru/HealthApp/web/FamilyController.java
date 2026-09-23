package ru.HealthApp.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.FamilyResponseDTO;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.dto.auth.VerificationRequestDTO;
import ru.HealthApp.config.UserPrincipal;
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

    @PostMapping("/{adminEmail}")
    public ResponseEntity<FamilyResponseDTO> createFamily(
            @AuthenticationPrincipal UserPrincipal member,
            @RequestBody CreateFamilyRequest request) {

        String invitedUserEmail = member.email();
        String adminEmail = request.adminEmail;
        String secretCode = request.secretCode;

        var familyDto = familyService.getFamilyResponseDtoForNewFamily(
                invitedUserEmail,
                adminEmail,
                secretCode
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(familyDto);
    }

    @PostMapping("/invite")
    public ResponseEntity<FamilyResponseDTO> inviteMember(
            @Valid @RequestBody VerificationRequestDTO request,
            @AuthenticationPrincipal UserPrincipal user) {
        String adminEmail = request.email();
        String invitedUserEmail = user.email();
        String code = request.code();

        var familyDto = familyService.getFamilyResponseDtoForExistedFamily(
          invitedUserEmail,
          adminEmail,
          code
        );

        return ResponseEntity.ok(familyDto);
    }

    @GetMapping("/is-no-family")
    public ResponseEntity<FamilyResponseDTO> isFamilyUser(@AuthenticationPrincipal UserPrincipal actor) {
        Long id = actor.userId();
        FamilyResponseDTO request = familyService.getUserFamilyInfo(id);
        return ResponseEntity.ok(request);
    }

    //invite doctor to family

    @PostMapping("/{familyId}/virtual-members")
    public ResponseEntity<UserResponseDTO> createVirtualMember(
            @PathVariable Long familyId,
            @RequestBody CreateVirtualMemberRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        
        UserResponseDTO virtualMember = familyService.getVirtualDto(
                familyId,
                request,
                user
        );
        
        return ResponseEntity.ok(virtualMember);
    }

    @GetMapping("/{familyId}/members")
    public ResponseEntity<List<UserResponseDTO>> getFamilyMembers(
            @PathVariable Long familyId,
            @AuthenticationPrincipal UserPrincipal user) {

        Long userId = user.userId();
        List<UserResponseDTO> members = familyService.getFamilyMembers(familyId, userId);

        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/{familyId}/members/{userEmail}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long familyId,
            @PathVariable String userEmail,
            @AuthenticationPrincipal UserPrincipal user) {
        familyService.removeMemberFromFamily(user.userId(), familyId, userEmail);
        return ResponseEntity.noContent().build();
    }

    public record CreateFamilyRequest(
            @NotNull
            String secretCode,
            
            @NotBlank(message = "Email обязателен")
            @Email(message = "Некорректный формат email")
            String adminEmail

    ) {}

    public record CreateVirtualMemberRequest(
            @NotBlank(message = "Имя не может быть пустым")
            @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 50 символов")
            String firstName
    ) {}


}
