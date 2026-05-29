package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.HealthApp.repository.InvitationRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ExpiredDataCleaner {

    private final InvitationRepository repository;

    @Scheduled(cron = "0 0 0 * * *")
    public void clear() {
        LocalDateTime nowDate = LocalDateTime.now();
        repository.deleteByExpirationTimestampBefore(nowDate);
    }
}
