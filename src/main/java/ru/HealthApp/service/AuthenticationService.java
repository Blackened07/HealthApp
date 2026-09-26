package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.auth.AuthResponseDto;
import ru.HealthApp.dto.auth.LoginRequestDto;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.entities.Account;
import ru.HealthApp.utils.JwtUtil;
import ru.HealthApp.utils.PasswordUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountLookupService accountLookupService;

    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        Account account = accountLookupService.findByEmail(request.email());

        if (!account.isEnabled()) {
            throw AccessDeniedException.getNotVerifyEmailException();
        }

        if (!PasswordUtil.matches(request.password(), account.getPassword())) {
            throw AccessDeniedException.getWrongEmailOrPassException();
        }

        String token = JwtUtil.generateToken(account.getEmail(), account.getId(), account.getRole());

        return new AuthResponseDto(token, account.getId(), account.getFirstName(), true);
    }

}
