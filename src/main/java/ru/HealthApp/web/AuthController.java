package ru.HealthApp.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.AccountResponseDTO;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.repository.entities.Account;
import ru.HealthApp.service.AccountService;
import ru.HealthApp.service.DoctorService;
import ru.HealthApp.service.UserService;
import ru.HealthApp.utils.JwtUtil;
import ru.HealthApp.utils.PasswordUtil;
import jakarta.validation.constraints.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponseDTO> registerAccount(@Valid @RequestBody RegisterAccountRequest request) {

        if (accountService.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().build();
        }

        AccountResponseDTO accDto = null;

        if (request.systemRole == Account.SystemRole.USER) {
             accDto = userService.createUser(
                    request.email(),
                    request.password(),
                    request.firstName()
            );
        }

        if (request.systemRole == Account.SystemRole.DOCTOR) {
            accDto = doctorService.createDoctor(
                    request.email(),
                    request.password(),
                    request.firstName()
            );
        }

        return ResponseEntity.ok(accDto);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            Account account = accountService.findByEmail(request.email());
            
            if (!PasswordUtil.matches(request.password(), account.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("Неверный email или пароль", false));
            }

            //по айди получить аккаунт и проверить гет класс доктор или юзер!

            //Разные токены для доктора и юзера

            String token = JwtUtil.generateToken(account.getEmail(), account.getId());
            
            return ResponseEntity.ok(new AuthResponse(token, true));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("Пользователь не найден", false));
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam String email) {
        boolean exists = accountService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponse(!exists));
    }

    public record RegisterAccountRequest(
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
            String firstName,

            @NotBlank(message = "Не выбрана роль в приложении")
            Account.SystemRole systemRole
    ) {}

    
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
