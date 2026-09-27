package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
final class GlucoseValidator implements IMetricValidationStrategy {

    @Override
    public void validate(HealthRecordRequestDTO data) {
        validateRange(data.value1(), HealthThresholds.GLUCOSE_VALID_MIN, HealthThresholds.GLUCOSE_VALID_MAX, "Сахар");
    }

    private void validateRange(Double value, double min, double max, String type) {
        if (value < min || value > max) {
            throw InvalidMetricException.outOfRange(type, min, max);
        }
    }
}
