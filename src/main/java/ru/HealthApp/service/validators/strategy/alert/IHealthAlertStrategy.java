package ru.HealthApp.service.validators.strategy.alert;

import ru.HealthApp.entities.HealthRecord;

public sealed interface IHealthAlertStrategy
        permits BloodPressureAlert, GlucoseAlert, TemperatureAlert, WeightAlert {
    AlertMessage check(HealthRecord record, String alertReason);

}
