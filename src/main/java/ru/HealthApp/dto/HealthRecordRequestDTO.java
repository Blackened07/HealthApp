package ru.HealthApp.dto;

import ru.HealthApp.service.exceptions.ExceptionMessage;

public record HealthRecordRequestDTO(String type, Double value1, Double value2, String note) {

    public HealthRecordRequestDTO {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(ExceptionMessage.MAIN_VALUE_ERROR.getMessage());
        }

        if (value1 == null || value1 <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.SUB_ZERO_VALUE.getMessage());
        }
    }
}
