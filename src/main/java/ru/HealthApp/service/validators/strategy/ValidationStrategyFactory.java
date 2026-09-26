package ru.HealthApp.service.validators.strategy;

import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthMetricType;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ValidationStrategyFactory {

    private final Map<HealthMetricType, MetricValidationStrategy> strategies;

    public ValidationStrategyFactory(
            BloodPressureValidator bloodPressureValidator,
            WeightValidator weightValidator,
            GlucoseValidator glucoseValidator,
            TemperatureValidator temperatureValidator,
            CustomValidator customValidator) {

        this.strategies = new EnumMap<>(HealthMetricType.class);
        strategies.put(HealthMetricType.BLOOD_PRESSURE, bloodPressureValidator);
        strategies.put(HealthMetricType.WEIGHT, weightValidator);
        strategies.put(HealthMetricType.GLUCOSE, glucoseValidator);
        strategies.put(HealthMetricType.TEMPERATURE, temperatureValidator);
        strategies.put(HealthMetricType.CUSTOM, customValidator);
    }

    public MetricValidationStrategy getStrategy(HealthMetricType type) {
        MetricValidationStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown metric type: " + type);
        }
        return strategy;
    }
}
