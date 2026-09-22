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
import ru.HealthApp.service.AccountService;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponseDTO> registerAccount(@Valid @RequestBody RegisterAccountRequestDto request) {

        AccountResponseDTO accountResponseDTO = accountService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {

        AuthResponseDto authResponseDto = accountService.login(request);

        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody VerificationRequestDTO request) {

        accountService.verify(request);

        return ResponseEntity.ok("Почта успешно подтверждена!");
    }

    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponseDto> checkEmail(@RequestParam String email) {
        boolean exists = accountService.existsByEmail(email);
        return ResponseEntity.ok(new EmailCheckResponseDto(!exists));
    }

}
