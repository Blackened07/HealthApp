package ru.HealthApp.web;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.mapper.HealthAppMapper;
import ru.HealthApp.entities.User;
import ru.HealthApp.service.AccountService;
import ru.HealthApp.service.UserService;
import ru.HealthApp.exceptions.IllegalActionException;
import ru.HealthApp.service.validators.AccessGuard;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final AccessGuard accessGuard;
    private final HealthAppMapper mapper;
    //TODO РИДЕР ЮЗЕР ЗАМЕНЯЕТСЯ НА ПРИНЦИПАЛА
    @GetMapping("/{targetUserId:\\d+}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long targetUserId, @RequestParam Long readerUserId) {

        User readerUser = userService.findById(readerUserId);
        User targetUser = userService.findById(targetUserId);
        
        accessGuard.checkReadAccess(readerUser, targetUser);
        
        return ResponseEntity.ok(mapper.toResponse(targetUser));
    }

    @GetMapping("/email/{adminEmail}")
    public ResponseEntity<Boolean> isAdminHaveFamily(@PathVariable String adminEmail){
        User admin = userService.findByEmail(adminEmail);

        if (admin.isNoFamily()) {
            return ResponseEntity.ok(false);
        }
        if (!admin.isAdmin()) {
            throw new IllegalActionException("Пользователь не является админом семьи");
        }

        return ResponseEntity.ok(true);
    }


    //getFamily - получить просто список членов семьи с именами и ролями; only for family members
    //if ADMIN -> open FamilyManager

    //записаться на приём
    //getDoctor


    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean available = !accountService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponse(available));
    }

    public record EmailCheckResponse(
            boolean available
    ) {}
}
