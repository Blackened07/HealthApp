package ru.HealthApp.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.EmailCheckResponseDTO;
import ru.HealthApp.dto.auth.AccountResponseDTO;
import ru.HealthApp.dto.auth.VerificationRequestDTO;
import ru.HealthApp.dto.auth.AuthResponseDTO;
import ru.HealthApp.dto.auth.LoginRequestDTO;
import ru.HealthApp.dto.auth.RegisterAccountRequestDTO;
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
    public ResponseEntity<AccountResponseDTO> registerAccount(@Valid @RequestBody RegisterAccountRequestDTO request) {

        AccountResponseDTO accountResponseDTO = registrationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        AuthResponseDTO authResponseDTO = authenticationService.login(request);

        return authResponseDTO.success() ?
                ResponseEntity.ok(authResponseDTO) :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponseDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody VerificationRequestDTO request) {
        verificationService.verify(request);
        return ResponseEntity.ok("Почта успешно подтверждена!");
    }

    @GetMapping("/sendAlert-email")
    public ResponseEntity<EmailCheckResponseDTO> checkEmail(@RequestParam String email) {
        boolean exists = registrationService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponseDTO(!exists));
    }

}
