package ru.HealthApp.dto;

import java.time.LocalDateTime;

public record HealthRecordResponseDTO(
    Long id,
    String type,
    Double value1,
    Double value2,
    String note,
    LocalDateTime timestamp,
    String userName)
{
}
