package ru.HealthApp.service.validators.strategy.validation;

import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthMetricType;
import ru.HealthApp.exceptions.InvalidMetricException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
class ValidationStrategyFactory {

    private final Map<HealthMetricType, IMetricValidationStrategy> strategies;

    ValidationStrategyFactory(
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

    IMetricValidationStrategy getStrategy(HealthMetricType type) {
        return Optional.ofNullable(strategies.get(type))
                .orElseThrow(InvalidMetricException::MetricNotFound);
    }
}
