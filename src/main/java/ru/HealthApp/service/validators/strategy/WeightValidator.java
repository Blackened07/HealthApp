package ru.HealthApp.service.validators.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.exceptions.InvalidMetricException;

@Component
@RequiredArgsConstructor
public class WeightValidator implements MetricValidationStrategy {

    private final HealthThresholds thresholds;

    @Override
    public void validate(HealthRecordRequestDTO data) {
        validateRange(data.value1(), HealthThresholds.WEIGHT_MIN, HealthThresholds.WEIGHT_MAX, "Масса");
    }

    private void validateRange(Double value, double min, double max, String type) {
        if (value < min || value > max) {
            throw InvalidMetricException.outOfRange(type, min, max);
        }
    }
}
