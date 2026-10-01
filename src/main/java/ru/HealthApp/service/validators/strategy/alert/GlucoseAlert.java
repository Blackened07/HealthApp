package ru.HealthApp.service.validators.strategy.alert;

import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.notifications.Message;

@Component
final class GlucoseAlert implements IHealthAlertStrategy {
    @Override
    public Message<HealthRecord> getAlertMessage(HealthRecord record, String alertReason) {
        double v1 = record.getValue1();

        if (isGlucoseCritical(v1)) {
            alertReason = ExceptionMessage.createMessageWithArgs(
                    ExceptionMessage.GLUCOSE_DANGER, v1
            );
        }

       return Message.create(alertReason, record);
    }

    private boolean isGlucoseCritical(Double value) {
        if (value == null) return false;
        return value > HealthThresholds.GLUCOSE_MAX || value < HealthThresholds.GLUCOSE_MIN;
    }
}
