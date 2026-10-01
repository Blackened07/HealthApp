package ru.HealthApp.service.validators.strategy.alert;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.notifications.Message;

@Component
final class TemperatureAlert implements IHealthAlertStrategy{
    @Override
    public Message<HealthRecord> getAlertMessage(HealthRecord record, String alertReason) {
        double v1 = record.getValue1();

        if (isTemperatureCritical(v1)) {
            alertReason = ExceptionMessage.createMessageWithArgs(
                    ExceptionMessage.TEMPERATURE_DANGER, v1
            );
        }

        return Message.create(alertReason, record);
    }

    private boolean isTemperatureCritical(Double value) {
        if (value == null) return false;
        return value > HealthThresholds.TEMPERATURE_MAX || value < HealthThresholds.TEMPERATURE_MIN;
    }

}
