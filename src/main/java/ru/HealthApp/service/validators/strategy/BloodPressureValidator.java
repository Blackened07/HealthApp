package ru.HealthApp.service.validators.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
@RequiredArgsConstructor
public class BloodPressureValidator implements MetricValidationStrategy {

    private final HealthThresholds thresholds;

    @Override
    public void validate(HealthRecordRequestDTO data) {
        if (data.value2() == null || data.value2() == 0) {
            throw InvalidMetricException.pressureValue2Required();
        }

        validateRange(data.value1(), HealthThresholds.PRESSURE_SYS_VALID_MIN, HealthThresholds.PRESSURE_SYS_VALID_MAX, "Верхнее давление");
        validateRange(data.value2(), HealthThresholds.PRESSURE_DIA_VALID_MIN, HealthThresholds.PRESSURE_DIA_VALID_MAX, "Нижнее давление");
    }

    private void validateRange(Double value, double min, double max, String type) {
        if (value < min || value > max) {
            throw InvalidMetricException.outOfRange(type, min, max);
        }
    }
}
