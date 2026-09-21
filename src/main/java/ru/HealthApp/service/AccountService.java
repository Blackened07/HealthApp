package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.AccountResponseDTO;
import ru.HealthApp.dto.UserResponseDTO;
import ru.HealthApp.dto.VerificationRequestDTO;
import ru.HealthApp.dto.auth.AuthResponseDto;
import ru.HealthApp.dto.auth.LoginRequestDto;
import ru.HealthApp.dto.auth.RegisterAccountRequestDto;
import ru.HealthApp.entities.FamilyRole;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.AccessDeniedException;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.repository.AccountRepository;
import ru.HealthApp.entities.Account;
import ru.HealthApp.utils.CodeGenerator;
import ru.HealthApp.utils.JwtUtil;
import ru.HealthApp.utils.PasswordUtil;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDTO register(RegisterAccountRequestDto request) {

        String email = request.email();

        if (existsByEmail(email)) {
            Account account = findByEmail(email);

            if (!account.isEnabled()) {
                return getNotEnabledAccount(request);
            }

            throw new AccessDeniedException(ExceptionMessage.EMAIL_ALREADY_VERIFY.getMessage());
        }

        return getAccount(request);
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {

        Account account = findByEmail(request.email());

        if (!account.isEnabled()) {
            throw new AccessDeniedException(ExceptionMessage.EMAIL_NOT_VERIFY.getMessage());
        }

        if (!PasswordUtil.matches(request.password(), account.getPassword())) {
            throw new AccessDeniedException(ExceptionMessage.WRONG_EMAIL_OR_PASS.getMessage());
        }

        String token = JwtUtil.generateToken(account.getEmail(), account.getId(), account.getRole());

        return new AuthResponseDto(token, account.getId(), account.getFirstName(), true);
    }

    @Transactional
    public void verify(VerificationRequestDTO request) {

        Account account = findByEmail(request.email());

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

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(email));
    }

    private AccountResponseDTO getAccount(RegisterAccountRequestDto request) {

        String code = CodeGenerator.generateVerificationEmailCode();

        return switch (request.systemRole()) {
            case DOCTOR -> getDoctorAccount(request, code);
            case USER -> getUserAccount(request, code);
        };
    }

    private AccountResponseDTO getUserAccount(RegisterAccountRequestDto request, String code){
        return userService.createUser(
                request.email(),
                request.password(),
                request.firstName(),
                code);
    }

    private AccountResponseDTO getDoctorAccount(RegisterAccountRequestDto request, String code){
        throw new AccessDeniedException("Пока нельзя зарегистрироваться как доктор");
        /*return doctorService.createDoctor(
                request.email(),
                request.password(),
                request.firstName());*/
    }

    private AccountResponseDTO getNotEnabledAccount(RegisterAccountRequestDto request) {
        return new UserResponseDTO(0L, request.email(), request.firstName(), FamilyRole.NO_FAMILY_USER, LocalDateTime.now());
    }

}
