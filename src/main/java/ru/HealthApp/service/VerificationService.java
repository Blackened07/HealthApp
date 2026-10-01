package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.auth.VerificationRequestDTO;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.entities.Account;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AccountLookupService accountLookupService;

    @Transactional
    public void verify(VerificationRequestDTO request) {
        String email = request.email();
        log.info("Попытка верификации для email: {}", email);

        Account account = accountLookupService.findByEmail(email);

        if (account.getVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Код верификации истек для email: {}", email);
            throw AccessDeniedException.getExpiredCodeException();
        }

        if (!account.getVerificationCode().equals(request.code())) {
            log.warn("Неверный код верификации для email: {}", email);
            throw AccessDeniedException.getWrongVerifyCodeException();
        }

        if (account.getRole() == Account.SystemRole.USER) {
            userService.saveVerify(account);
            log.info("Успешная верификация пользователя: {}", email);
        } else if (account.getRole() == Account.SystemRole.DOCTOR) {
            doctorService.saveVerify(account);
            log.info("Успешная верификация доктора: {}", email);
        }
    }
}
