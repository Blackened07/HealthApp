package ru.HealthApp.service.validators.strategy.alert;

import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthMetricType;
import ru.HealthApp.exceptions.InvalidMetricException;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class HealthAlertFactory {
    private final Map<HealthMetricType, IHealthAlertStrategy> strategies;

    HealthAlertFactory(
            BloodPressureAlert bloodPressureAlert,
            GlucoseAlert glucoseAlert,
            TemperatureAlert temperatureAlert,
            WeightAlert weightAlert
    ) {
        this.strategies = new EnumMap<>(HealthMetricType.class);
        strategies.put(HealthMetricType.BLOOD_PRESSURE, bloodPressureAlert);
        strategies.put(HealthMetricType.GLUCOSE, glucoseAlert);
        strategies.put(HealthMetricType.TEMPERATURE, temperatureAlert);
        strategies.put(HealthMetricType.WEIGHT, weightAlert);
    }

    IHealthAlertStrategy getStrategy(HealthMetricType type) {
        return Optional.ofNullable(strategies.get(type))
                .orElseThrow(InvalidMetricException::MetricNotFound);
    }
}
