package ru.HealthApp.service.validators.strategy.alert;

import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthRecord;

@Component
final class WeightAlert implements IHealthAlertStrategy{
    @Override
    public AlertMessage getAlertMessage(HealthRecord record, String alertReason) {
        return AlertMessage.create(alertReason);
    }
}
