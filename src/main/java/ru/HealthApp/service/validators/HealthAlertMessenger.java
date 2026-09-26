package ru.HealthApp.service.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.config.HealthThresholds;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.entities.User;
import ru.HealthApp.exceptions.ExceptionMessage;


@Component
@RequiredArgsConstructor
public class HealthAlertMessenger {

    private final HealthThresholds thresholds;

    public void check(HealthRecord record) {

        String alertReason = "";
        boolean isCritical = false;
        double v1 = record.getValue1();
        double v2 = record.getValue2();

        switch (record.getMetricType()) {
            case BLOOD_PRESSURE -> {
                if (isPressureCritical(v1, v2)) {
                    isCritical = true;
                    alertReason = ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.PRESSURE_DANGER,
                            v1, v2
                    );
                }
            }
            case GLUCOSE -> {
                if (isGlucoseCritical(v1)) {
                    isCritical = true;
                    alertReason = ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.GLUCOSE_DANGER, v1
                    );
                }
            }
            case TEMPERATURE -> {
                if (isTemperatureCritical(v1)) {
                    isCritical = true;
                    alertReason = ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.TEMPERATURE_DANGER, v1
                    );
                }
            }
            case WEIGHT -> {}
        }

        if (isCritical) {
            sendAdminAlert(record, alertReason);
        }

    }

    private boolean isPressureCritical(Double sys, Double dia) {
        if (sys == null || dia == null) return false;
        return sys > HealthThresholds.PRESSURE_SYS_MAX || sys < HealthThresholds.PRESSURE_SYS_MIN
                || dia > HealthThresholds.PRESSURE_DIA_MAX || dia < HealthThresholds.PRESSURE_DIA_MIN;
    }

    private boolean isGlucoseCritical(Double value) {
        if (value == null) return false;
        return value > HealthThresholds.GLUCOSE_MAX || value < HealthThresholds.GLUCOSE_MIN;
    }

    private boolean isTemperatureCritical(Double value) {
        if (value == null) return false;
        return value > HealthThresholds.TEMPERATURE_MAX || value < HealthThresholds.TEMPERATURE_MIN;
    }

    private void sendAdminAlert(HealthRecord record, String reason) {
        User userAdmin = record.getAdminUserOfThisRecord();

        if (userAdmin == null) {
            return;
        }

        System.err.println("!!! [ALARM] ДЛЯ: " + userAdmin.getFirstName());
        System.err.println("У " + record.getUserName() + " обнаружено: " + reason);
        System.err.println("--------------------------------------------------");
    }

}
