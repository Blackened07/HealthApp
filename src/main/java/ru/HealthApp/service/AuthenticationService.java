package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.auth.AuthResponseDTO;
import ru.HealthApp.dto.auth.LoginRequestDTO;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.entities.Account;
import ru.HealthApp.utils.JwtUtil;
import ru.HealthApp.utils.PasswordUtil;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountLookupService accountLookupService;

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        String email = request.email();
        log.info("Попытка входа для email: {}", email);

        Account account = accountLookupService.findByEmail(email);

        if (!account.isEnabled()) {
            log.warn("Попытка входа с неверифицированным аккаунтом: {}", email);
            return new AuthResponseDTO("NEED_VERIFICATION", -1L, "Unknown", false);
        }

        if (!PasswordUtil.matches(request.password(), account.getPassword())) {
            log.warn("Неверный пароль для email: {}", email);
            throw AccessDeniedException.getWrongEmailOrPassException();
        }

        String token = JwtUtil.generateToken(account.getEmail(), account.getId(), account.getRole());
        log.info("Успешный вход для email: {}, роль: {}", email, account.getRole());

        User user = (User) account;
        user.setLastActivity(LocalDateTime.now());

        return new AuthResponseDTO(token, account.getId(), account.getFirstName(), true);
    }

}
