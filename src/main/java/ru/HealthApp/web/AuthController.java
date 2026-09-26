package ru.HealthApp.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.EmailCheckResponseDto;
import ru.HealthApp.dto.auth.AccountResponseDTO;
import ru.HealthApp.dto.auth.VerificationRequestDTO;
import ru.HealthApp.dto.auth.AuthResponseDto;
import ru.HealthApp.dto.auth.LoginRequestDto;
import ru.HealthApp.dto.auth.RegisterAccountRequestDto;
import ru.HealthApp.service.RegistrationService;
import ru.HealthApp.service.AuthenticationService;
import ru.HealthApp.service.VerificationService;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final VerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponseDTO> registerAccount(@Valid @RequestBody RegisterAccountRequestDto request) {

        AccountResponseDTO accountResponseDTO = registrationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {

        AuthResponseDto authResponseDto = authenticationService.login(request);

        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody VerificationRequestDTO request) {

        verificationService.verify(request);

        return ResponseEntity.ok("Почта успешно подтверждена!");
    }

    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponseDto> checkEmail(@RequestParam String email) {
        boolean exists = registrationService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponseDto(!exists));
    }

}
