package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
final class WeightValidator implements IMetricValidationStrategy {

    @Override
    public boolean validate(HealthRecord data) {
        validateRange(data.getValue1());
        return true;
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
