package ru.HealthApp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.UserService;
import ru.HealthApp.utils.JwtUtil;
import ru.HealthApp.utils.PasswordUtil;
import jakarta.validation.constraints.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.findByEmail(request.email());
            
            if (!PasswordUtil.matches(request.password(), user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("Неверный email или пароль", false));
            }

            String token = JwtUtil.generateToken(user.getEmail(), user.getId());
            
            return ResponseEntity.ok(new AuthResponse(token, true));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("Пользователь не найден", false));
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponse(!exists));
    }

    
    public record LoginRequest(
            @NotBlank(message = "Email не может быть пустым")
            @Email(message = "Некорректный формат email")
            String email,
            
            @NotBlank(message = "Пароль не может быть пустым")
            String password
    ) {}

    public record AuthResponse(String message, boolean success) {}

    public record EmailCheckResponse(boolean exists) {}
}
