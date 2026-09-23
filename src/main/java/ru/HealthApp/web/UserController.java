package ru.HealthApp.web;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.config.UserPrincipal;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{targetUserId:\\d+}")
    public ResponseEntity<UserResponseDTO> getUser(
            @PathVariable Long targetUserId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        var userResponseDto = userService.getUser(targetUserId, userPrincipal);
        return ResponseEntity.ok(userResponseDto);
    }


}
