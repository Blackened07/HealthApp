package ru.HealthApp.service.validators.strategy.alert;

import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.notifications.Message;

sealed interface IHealthAlertStrategy
        permits BloodPressureAlert, GlucoseAlert, TemperatureAlert, WeightAlert {

    Message<HealthRecord> getAlertMessage(HealthRecord record, String alertReason);

}
