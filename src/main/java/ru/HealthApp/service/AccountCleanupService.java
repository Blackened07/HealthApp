package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.repository.AccountRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCleanupService {

    private final AccountRepository accountRepository;

    @Scheduled(fixedRate = 3600000) // Запуск каждый час (3600000 мс)
    @Transactional
    public void deleteUnverifiedAccounts() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        
        var accountsToDelete = accountRepository.findUnverifiedAccountsOlderThan(threshold);
        
        if (!accountsToDelete.isEmpty()) {
            log.info("Найдено {} неверифицированных аккаунтов для удаления", accountsToDelete.size());
            accountRepository.deleteUnverifiedAccountsOlderThan(threshold);
            log.info("Удалено {} неверифицированных аккаунтов", accountsToDelete.size());
        }
    }
}
