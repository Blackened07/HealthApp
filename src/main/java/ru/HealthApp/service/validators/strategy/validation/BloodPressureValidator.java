package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
final class BloodPressureValidator implements IMetricValidationStrategy {

    @Override
    public boolean validate(HealthRecord data) {
        if (data.getValue1() == null || data.getValue2() == 0) {
            throw InvalidMetricException.pressureValue2Required();
        }

        validateRange(data.getValue1(), HealthThresholds.PRESSURE_SYS_VALID_MIN, HealthThresholds.PRESSURE_SYS_VALID_MAX, "Верхнее давление");
        validateRange(data.getValue2(), HealthThresholds.PRESSURE_DIA_VALID_MIN, HealthThresholds.PRESSURE_DIA_VALID_MAX, "Нижнее давление");

        return true;
    }

    private void validateRange(Double value, double min, double max, String type) {
        if (value < min || value > max) {
            throw InvalidMetricException.outOfRange(type, min, max);
        }
    }
}
