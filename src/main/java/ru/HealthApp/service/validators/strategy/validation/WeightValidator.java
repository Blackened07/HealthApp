package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
final class WeightValidator implements IMetricValidationStrategy {

    @Override
    public void validate(HealthRecordRequestDTO data) {
        validateRange(data.value1());
    }

    private void validateRange(Double value) {
        if (value < HealthThresholds.WEIGHT_MIN ||
                value > HealthThresholds.WEIGHT_MAX) {
            throw InvalidMetricException
                    .outOfRange(
                            "Масса",
                            HealthThresholds.WEIGHT_MIN, HealthThresholds.WEIGHT_MAX);
        }
    }
}
