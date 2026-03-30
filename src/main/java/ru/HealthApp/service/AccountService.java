package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.repository.AccountRepository;
import ru.HealthApp.repository.DoctorRepository;
import ru.HealthApp.repository.entities.Account;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final DoctorRepository doctorRepository;
    private final HealthRecordMapper mapper;

    //call repo methods

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }


}
