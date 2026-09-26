package ru.HealthApp.mapper.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class DateTimeConverter {

    public LocalDateTime convertMillisToLocalDateTime(long timestampMillis, int zoneOffsetMillis) {
        int offsetInSeconds = Math.toIntExact(java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(zoneOffsetMillis));
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetInSeconds);
        Instant instant = Instant.ofEpochMilli(timestampMillis);
        return LocalDateTime.ofInstant(instant, offset);
    }
}
