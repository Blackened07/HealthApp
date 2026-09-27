package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.auth.AuthResponseDto;
import ru.HealthApp.dto.auth.LoginRequestDto;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.entities.Account;
import ru.HealthApp.utils.JwtUtil;
import ru.HealthApp.utils.PasswordUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountLookupService accountLookupService;

    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        String email = request.email();
        log.info("Попытка входа для email: {}", email);

        Account account = accountLookupService.findByEmail(email);

        if (!account.isEnabled()) {
            log.warn("Попытка входа с неверифицированным аккаунтом: {}", email);
            throw AccessDeniedException.getNotVerifyEmailException();
        }

        if (!PasswordUtil.matches(request.password(), account.getPassword())) {
            log.warn("Неверный пароль для email: {}", email);
            throw AccessDeniedException.getWrongEmailOrPassException();
        }

        String token = JwtUtil.generateToken(account.getEmail(), account.getId(), account.getRole());
        log.info("Успешный вход для email: {}, роль: {}", email, account.getRole());

        return new AuthResponseDto(token, account.getId(), account.getFirstName(), true);
    }

}
