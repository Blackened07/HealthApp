package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.auth.VerificationRequestDTO;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.service.AccountLookupService;
import ru.HealthApp.entities.Account;
import ru.HealthApp.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AccountLookupService accountLookupService;

    @Transactional
    public void verify(VerificationRequestDTO request) {
        Account account = accountLookupService.findByEmail(request.email());

        if (account.getVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AccessDeniedException(ExceptionMessage.EXPIRED_CODE.getMessage());
        }

        if (!account.getVerificationCode().equals(request.code())) {
            throw new AccessDeniedException(ExceptionMessage.WRONG_VERIFY_CODE.getMessage());
        }

        if (account.getRole() == Account.SystemRole.USER) {
            userService.saveVerify(account);
        } else if (account.getRole() == Account.SystemRole.DOCTOR) {
            doctorService.saveVerify(account);
        }
    }
}
