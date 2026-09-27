package ru.HealthApp.service.validators.strategy.validation;

import ru.HealthApp.dto.HealthRecordRequestDTO;

public sealed interface IMetricValidationStrategy
        permits BloodPressureValidator, GlucoseValidator, TemperatureValidator, WeightValidator {

    void validate(HealthRecordRequestDTO data);
}
