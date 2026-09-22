package ru.HealthApp.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.FamilyInvitationRequestDTO;
import ru.HealthApp.config.UserPrincipal;
import ru.HealthApp.dto.FamilyInvitationResponseDto;
import ru.HealthApp.service.InvitationService;

@RestController
@RequestMapping("/api/v1/invitation")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    /**
     * Метод isAdminCanInviteToFamily вызывается после создания приглашения, когда юзер отвечает на приглашение
     * Метод отвечает на вопрос кто пригласил! Приглашение выслано для создания семьи или для вступления в существующую
     **/
    @GetMapping("/email/{adminEmail}")
    public ResponseEntity<Boolean> isAdminCanInviteToFamily(
            @PathVariable String adminEmail,
            @AuthenticationPrincipal UserPrincipal userPrincipal){
        boolean isInvitationRight = invitationService.isInvitationToExistFamilySuccess(adminEmail, userPrincipal);
        return ResponseEntity.ok(isInvitationRight);
    }

    @PostMapping
    public ResponseEntity<FamilyInvitationResponseDto> createInvitation(
            @AuthenticationPrincipal UserPrincipal actor,
            @Valid @RequestBody FamilyInvitationRequestDTO request) {
        var response = invitationService.createInvitation(actor, request);
        return ResponseEntity.ok(response);
    }
}
