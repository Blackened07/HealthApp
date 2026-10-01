package ru.HealthApp.service.validators.strategy.alert;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.notifications.Message;

@Component
final class WeightAlert implements IHealthAlertStrategy{
    @Override
    public Message<HealthRecord> getAlertMessage(HealthRecord record, String alertReason) {
        return Message.create(alertReason, record);
    }
}
