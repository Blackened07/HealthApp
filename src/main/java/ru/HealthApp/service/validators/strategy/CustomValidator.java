package ru.HealthApp.service.validators.strategy;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
public class CustomValidator implements MetricValidationStrategy {

    @Override
    public void validate(HealthRecordRequestDTO data) {
        if (data.value1() <= 0) {
            throw InvalidMetricException.subZeroValue();
        }
    }
}
