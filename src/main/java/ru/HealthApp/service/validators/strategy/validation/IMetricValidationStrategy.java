package ru.HealthApp.service.validators.strategy.validation;

import ru.HealthApp.entities.HealthRecord;

public sealed interface IMetricValidationStrategy
        permits BloodPressureValidator, GlucoseValidator, TemperatureValidator, WeightValidator {

    boolean validate(HealthRecord data);
}
