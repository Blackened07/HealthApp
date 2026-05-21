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
import ru.HealthApp.security.UserPrincipal;
import ru.HealthApp.service.InvitationService;

@RestController
@RequestMapping("/api/v1/invitation")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<FamilyInvitationResponseDTO> create(
            @AuthenticationPrincipal UserPrincipal actor,
            @Valid @RequestBody  FamilyInvitationRequestDTO request) {

        Long actorId = actor.userId();
        String actorEmail= actor.email();

        String invitedUserEmail = request.invitedUserEmail();
        String familyName = request.familyName();

        if (!isBothUsersFamilyMembers(actorId, invitedUserEmail)) {
            return ResponseEntity.badRequest().build();
        }

        String secretCode ="BURN IN HELL";

        invitationService.create(secretCode, invitedUserEmail, familyName, actorEmail);

        FamilyInvitationResponseDTO response = new FamilyInvitationResponseDTO(secretCode);

        return ResponseEntity.ok(response);

    }



    public record FamilyInvitationResponseDTO(
            String secretCode
    ){}

    private boolean isBothUsersFamilyMembers(Long actorId, String invitedUserEmail) {
        return invitationService.isUserFamilyMember(actorId) || invitationService.isUserFamilyMember(invitedUserEmail);
    }
}
