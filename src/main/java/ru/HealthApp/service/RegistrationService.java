package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.auth.AccountResponseDTO;
import ru.HealthApp.dto.auth.RegisterAccountRequestDto;
import ru.HealthApp.dto.auth.UserResponseDTO;
import ru.HealthApp.entities.FamilyRole;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.entities.Account;
import ru.HealthApp.utils.CodeGenerator;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AccountLookupService accountLookupService;

    @Transactional
    public AccountResponseDTO register(RegisterAccountRequestDto request) {
        String email = request.email();

        if (existsByEmail(email)) {
            Account account = accountLookupService.findByEmail(email);

            if (!account.isEnabled()) {
                return getNotEnabledAccount(request);
            }

            throw AccessDeniedException.getAlreadyVerifyEmailException();
        }

        return getAccount(request);
    }

    public boolean existsByEmail(String email) {
        return accountLookupService.existsByEmail(email);
    }

    private AccountResponseDTO getAccount(RegisterAccountRequestDto request) {
        String code = CodeGenerator.generateVerificationEmailCode();

        return switch (request.systemRole()) {
            case DOCTOR -> getDoctorAccount(request, code);
            case USER -> getUserAccount(request, code);
        };
    }

    private AccountResponseDTO getUserAccount(RegisterAccountRequestDto request, String code) {
        return userService.createUser(
                request.email(),
                request.password(),
                request.firstName(),
                code);
    }

    private AccountResponseDTO getDoctorAccount(RegisterAccountRequestDto request, String code) {
        throw new AccessDeniedException("Пока нельзя зарегистрироваться как доктор");
    }

    private AccountResponseDTO getNotEnabledAccount(RegisterAccountRequestDto request) {
        return new UserResponseDTO(0L, request.email(), request.firstName(), FamilyRole.NO_FAMILY_USER, LocalDateTime.now());
    }
}
