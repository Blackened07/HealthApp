package ru.HealthApp.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.HealthApp.dto.FamilyInvitationRequestDTO;
import ru.HealthApp.config.UserPrincipal;
import ru.HealthApp.service.FamilyService;
import ru.HealthApp.service.InvitationService;
import ru.HealthApp.service.UserService;
import ru.HealthApp.exceptions.IllegalActionException;

@RestController
@RequestMapping("/api/v1/invitation")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final UserService userService;
    private final FamilyService familyService;
    @PostMapping
    public ResponseEntity<FamilyInvitationResponseDTO> create(
            @AuthenticationPrincipal UserPrincipal actor,
            @Valid @RequestBody FamilyInvitationRequestDTO request) {

        Long actorId = actor.userId();
        String actorEmail = actor.email();

        String invitedUserEmail = request.invitedUserEmail();
        String familyName = request.familyName();

        userService.findByEmail(invitedUserEmail);

        if (invitationService.isInvitationExist(actorEmail)) {
            throw new IllegalActionException("Вы уже отправили приглашение");
        }

        /*if (!isBothUsersFamilyMembers(actorId, invitedUserEmail)) {
            return ResponseEntity.badRequest().build();
        }*/
        if (invitationService.isUserFamilyMember(invitedUserEmail)) {
            return ResponseEntity.badRequest().build();
        }

        String secretCode = invitationService.generateSecretCode();


        invitationService.create(secretCode, invitedUserEmail, familyName, actorEmail);


        FamilyInvitationResponseDTO response = new FamilyInvitationResponseDTO(secretCode);

        return ResponseEntity.ok(response);
    }

    private void createInvitationWithFamilyCreation() {
    }

    public record FamilyInvitationResponseDTO(
            String secretCode
    ) {}

    private boolean isBothUsersFamilyMembers(Long actorId, String invitedUserEmail) {
        return invitationService.isUserFamilyMember(actorId) || invitationService.isUserFamilyMember(invitedUserEmail);
    }
}
