package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
final class GlucoseValidator implements IMetricValidationStrategy {

    @Override
    public boolean validate(HealthRecord data) {

        validateRange(data.getValue1());

        return true;
    }

    private void validateRange(Double value) {
        if (value < HealthThresholds.GLUCOSE_VALID_MIN || value > HealthThresholds.GLUCOSE_VALID_MAX) {
            throw InvalidMetricException
                    .outOfRange(
                            "Сахар",
                            HealthThresholds.GLUCOSE_VALID_MIN,
                            HealthThresholds.GLUCOSE_VALID_MAX);
        }

    }
}
