package ru.HealthApp.service.validators.strategy.alert;

import ru.HealthApp.entities.HealthMetricType;
import ru.HealthApp.exceptions.InvalidMetricException;

import java.util.EnumMap;
import java.util.Map;

public class HealthAlertFactory {
    private final Map<HealthMetricType, IHealthAlertStrategy> strategies;

    public HealthAlertFactory(
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

    public IHealthAlertStrategy getStrategy(HealthMetricType type) {
        IHealthAlertStrategy healthMetricType = strategies.get(type);
        if (healthMetricType == null) {
            throw InvalidMetricException.MetricNotFound();
        }
        return healthMetricType;
    }
}
