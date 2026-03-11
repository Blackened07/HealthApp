package ru.HealthApp.dto;

import jakarta.validation.constraints.*;

public record HealthRecordRequestDTO(
        @NotBlank(message = "Тип метрики обязателен")
        @Pattern(regexp = "^(BLOOD_PRESSURE|GLUCOSE|TEMPERATURE|WEIGHT|CUSTOM)$", 
                 message = "Недопустимый тип метрики. Используйте: BLOOD_PRESSURE, GLUCOSE, TEMPERATURE, WEIGHT, CUSTOM")
        String type,
        
        @NotNull(message = "Основное значение обязательно")
        @DecimalMin(value = "0.0", message = "Значение должно быть положительным")
        @DecimalMax(value = "999.9", message = "Слишком большое значение")
        Double value1,
        
        @DecimalMin(value = "0.0", message = "Значение должно быть положительным")
        @DecimalMax(value = "999.9", message = "Слишком большое значение")
        Double value2,
        
        @Size(max = 500, message = "Примечание не более 500 символов")
        String note
) {}
