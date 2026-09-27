package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthMetricType;
import ru.HealthApp.exceptions.InvalidMetricException;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ValidationStrategyFactory {

    private final Map<HealthMetricType, IMetricValidationStrategy> strategies;

    public ValidationStrategyFactory(
            BloodPressureValidator bloodPressureValidator,
            WeightValidator weightValidator,
            GlucoseValidator glucoseValidator,
            TemperatureValidator temperatureValidator) {

        this.strategies = new EnumMap<>(HealthMetricType.class);
        strategies.put(HealthMetricType.BLOOD_PRESSURE, bloodPressureValidator);
        strategies.put(HealthMetricType.WEIGHT, weightValidator);
        strategies.put(HealthMetricType.GLUCOSE, glucoseValidator);
        strategies.put(HealthMetricType.TEMPERATURE, temperatureValidator);
    }

    public IMetricValidationStrategy getStrategy(HealthMetricType type) {
        IMetricValidationStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new InvalidMetricException("Unknown metric type: " + type);
        }
        return strategy;
    }
}
