package ru.HealthApp.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.UserService;
import ru.HealthApp.service.UserPermissionService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserPermissionService userPermissionService;


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterUserRequest request) {

        if (userService.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.createUser(
                request.email(),
                request.password(),
                request.firstName()
        );
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId, @RequestParam Long currentUserId) {
        User currentUser = userService.findById(currentUserId);
        User targetUser = userService.findById(userId);
        
        if (!userPermissionService.canBeReadBy(targetUser, currentUser)) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(targetUser);
    }


    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean available = !userService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponse(available));
    }

    // DTO классы для запросов
    public record RegisterUserRequest(
            @NotBlank(message = "Email не может быть пустым")
            @Email(message = "Некорректный формат email")
            String email,

            @NotBlank(message = "Пароль не может быть пустым")
            @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$",
                    message = "Пароль должен содержать буквы и цифры")
            String password,

            @NotBlank(message = "Имя не может быть пустым")
            @Size(min = 2, max = 20, message = "Им должно быть от 2 до 20 символов")
            String firstName
    ) {}

    public record EmailCheckResponse(
            boolean available
    ) {}
}
