package ru.HealthApp.service.validators.strategy.alert;

import ru.HealthApp.entities.HealthRecord;

public final class WeightAlert implements IHealthAlertStrategy{
    @Override
    public AlertMessage check(HealthRecord record, String alertReason) {
        return AlertMessage.create(alertReason);
    }
}
