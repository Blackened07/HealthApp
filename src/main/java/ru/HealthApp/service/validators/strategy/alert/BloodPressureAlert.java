package ru.HealthApp.service.validators.strategy.alert;


import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.exceptions.ExceptionMessage;
import ru.HealthApp.notifications.Message;

@Component
final class BloodPressureAlert implements IHealthAlertStrategy{

    @Override
    public Message<HealthRecord> getAlertMessage(HealthRecord record, String alertReason) {
        double v1 = record.getValue1();
        double v2 = record.getValue2();
        if (isPressureCritical(v1, v2)) {
            alertReason = ExceptionMessage.createMessageWithArgs(
                    ExceptionMessage.PRESSURE_DANGER,
                    v1, v2
            );
        }
        return Message.create(alertReason, record);
    }

    private boolean isPressureCritical(Double sys, Double dia) {
        if (sys == null || dia == null) return false;
        return sys > HealthThresholds.PRESSURE_SYS_MAX || sys < HealthThresholds.PRESSURE_SYS_MIN
                || dia > HealthThresholds.PRESSURE_DIA_MAX || dia < HealthThresholds.PRESSURE_DIA_MIN;
    }
}
