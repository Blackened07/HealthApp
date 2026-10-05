package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.auth.AccountResponseDTO;
import ru.HealthApp.dto.auth.RegisterAccountRequestDTO;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.entities.FamilyRole;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.entities.Account;
import ru.HealthApp.utils.CodeGenerator;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AccountLookupService accountLookupService;

    @Transactional
    public AccountResponseDTO register(RegisterAccountRequestDTO request) {
        String email = request.email();
        log.info("Попытка регистрации для email: {}", email);

        if (existsByEmail(email)) {
            Account account = accountLookupService.findByEmail(email);

            if (!account.isEnabled()) {
                log.warn("Аккаунт уже существует, но не верифицирован: {}", email);
                return getNotEnabledAccount(request);
            }

            log.warn("Попытка регистрации с уже верифицированным email: {}", email);
            throw AccessDeniedException.getAlreadyVerifyEmailException();
        }

        AccountResponseDTO result = getAccount(request);
        log.info("Успешная регистрация для email: {}, роль: {}", email, request.systemRole());
        return result;
    }

    public boolean existsByEmail(String email) {
        return accountLookupService.existsByEmail(email);
    }

    private AccountResponseDTO getAccount(RegisterAccountRequestDTO request) {
        String code = CodeGenerator.generateVerificationEmailCode();

        return switch (request.systemRole()) {
            case DOCTOR -> getDoctorAccount(request, code);
            case USER -> getUserAccount(request, code);
        };
    }

    private AccountResponseDTO getUserAccount(RegisterAccountRequestDTO request, String code) {
        return userService.createUser(
                request.email(),
                request.password(),
                request.firstName(),
                code);
    }

    private AccountResponseDTO getDoctorAccount(RegisterAccountRequestDTO request, String code) {
        throw AccessDeniedException.getDoctorRegistrationDisabledException();
    }

    private AccountResponseDTO getNotEnabledAccount(RegisterAccountRequestDTO request) {
        return new UserResponseDTO(0L, request.email(), request.firstName(), FamilyRole.NO_FAMILY_USER, LocalDateTime.now());
    }
}
