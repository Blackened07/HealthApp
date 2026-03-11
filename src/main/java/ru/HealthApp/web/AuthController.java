package ru.HealthApp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.UserService;
import jakarta.validation.constraints.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        if (userService.existsByEmail(request.email())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("Пользователь с таким email уже существует", false));
        }

        User user = userService.createUser(
                request.email(),
                request.password(),
                request.firstName()
        );

        return ResponseEntity.ok(new AuthResponse("Пользователь успешно зарегистрирован", true));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.findByEmail(request.email());
            
            // TODO: Добавить проверку пароля (BCrypt)
            if (!user.getPassword().equals(request.password())) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("Неверный email или пароль", false));
            }

            return ResponseEntity.ok(new AuthResponse("Вход выполнен успешно", true));
            
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


    public record RegisterRequest(
            @NotBlank(message = "Email не может быть пустым")
            @Email(message = "Некорректный формат email")
            String email,
            
            @NotBlank(message = "Пароль не может быть пустым")
            @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
            String password,
            
            @NotBlank(message = "Имя не может быть пустым")
            @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
            String firstName
    ) {}

    public record LoginRequest(
            @NotBlank(message = "Email не может быть пустым")
            @Email(message = "Некорректный формат email")
            String email,
            
            @NotBlank(message = "Пароль не может быть пустым")
            String password
    ) {}

    public record AuthResponse(
            String message,
            boolean success
    ) {}

    public record EmailCheckResponse(
            boolean available
    ) {}
}
