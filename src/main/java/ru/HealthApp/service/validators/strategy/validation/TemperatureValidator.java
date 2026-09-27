package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
final class TemperatureValidator implements IMetricValidationStrategy {

    @Override
    public void validate(HealthRecordRequestDTO data) {
        validateRange(data.value1());
    }

    private void validateRange(Double value) {
        if (value < HealthThresholds.TEMPERATURE_VALID_MIN ||
                value > HealthThresholds.TEMPERATURE_VALID_MAX) {
            throw InvalidMetricException
                    .outOfRange(
                            "Температура",
                            HealthThresholds.TEMPERATURE_VALID_MIN, HealthThresholds.TEMPERATURE_VALID_MAX);
        }
    }
}
