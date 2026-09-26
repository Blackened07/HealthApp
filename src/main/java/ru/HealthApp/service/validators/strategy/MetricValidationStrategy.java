package ru.HealthApp.service.validators.strategy;

import ru.HealthApp.dto.HealthRecordRequestDTO;

public interface MetricValidationStrategy {

    void validate(HealthRecordRequestDTO data);
}
