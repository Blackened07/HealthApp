package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
import ru.HealthApp.repository.entities.HealthMetricType;
import ru.HealthApp.repository.entities.HealthRecord;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.ExceptionMessage;


@Component
public class HealthAlertMessenger {


    public void check(HealthRecord record) {

        String alertReason = "";
        boolean isDangerous = false;

        HealthMetricType metricType = HealthMetricType.fromString(record.getType());

        switch (metricType) {
            case BLOOD_PRESSURE -> {
                double v1 = record.getValue1();
                double v2 = record.getValue2();

                if (isPressureCritical(v1, v2)) {
                    isDangerous = true;
                    alertReason = ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.PRESSURE_DANGER,
                            v1, v2
                    );
                }
            }
            case GLUCOSE -> {
                double v = record.getValue1();

                if (isGlucoseCritical(v)) {
                    isDangerous = true;
                    alertReason = ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.GLUCOSE_DANGER, v
                    );
                }
            }
            case TEMPERATURE -> {
                double v = record.getValue1();

                if (isTemperatureCritical(v)) {
                    isDangerous = true;
                    alertReason = ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.TEMPERATURE_DANGER, v
                    );
                }
            }
            case WEIGHT -> {}
        }

        if (isDangerous) {
            sendAdminAlert(record, alertReason);
        }
     }

    private boolean isPressureCritical(Double sys, Double dia) {
        if (sys == null || dia == null) return false;
        return sys > 170 || sys < 80 || dia > 110 || dia < 40;
    }

    private boolean isGlucoseCritical(Double value) {
        if (value == null) return false;
        return value > 13.0 || value < 3.5;
    }

    private boolean isTemperatureCritical(Double value) {
        if (value == null) return false;
        return value > 39.0 || value < 35.0;
    }

    private void sendAdminAlert(HealthRecord record, String reason) {
        User userAdmin = record.getAdmin();

        if (userAdmin == null) {
            return;
        }

        System.err.println("!!! [ALARM] ДЛЯ: " + userAdmin.getFirstName());
        System.err.println("У " + record.getUserName() + " обнаружено: " + reason);
        System.err.println("--------------------------------------------------");
    }

}
