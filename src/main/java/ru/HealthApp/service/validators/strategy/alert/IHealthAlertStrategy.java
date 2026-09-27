package ru.HealthApp.service.validators.strategy.alert;

import ru.HealthApp.entities.HealthRecord;

sealed interface IHealthAlertStrategy
        permits BloodPressureAlert, GlucoseAlert, TemperatureAlert, WeightAlert {

    AlertMessage getAlertMessage(HealthRecord record, String alertReason);

}
