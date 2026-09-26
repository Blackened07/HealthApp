package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.entities.Account;
import ru.HealthApp.exceptions.ResourceNotFoundException;
import ru.HealthApp.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountLookupService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(email));
    }
}
