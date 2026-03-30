package ru.HealthApp.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.repository.entities.Account;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.AccountService;
import ru.HealthApp.service.DoctorService;
import ru.HealthApp.service.UserService;
import ru.HealthApp.service.validators.AccessGuard;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final AccessGuard accessGuard;
    private final HealthRecordMapper mapper;

    @GetMapping("/{targetUserId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long targetUserId, @RequestParam Long readerUserId) {

        User readerUser = userService.findById(readerUserId);
        User targetUser = userService.findById(targetUserId);
        
        accessGuard.checkReadAccess(readerUser, targetUser);
        
        return ResponseEntity.ok(mapper.toResponse(targetUser));
    }


    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean available = !accountService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponse(available));
    }

    public record EmailCheckResponse(
            boolean available
    ) {}
}
