package ru.HealthApp.service.validators.strategy.alert;

import ru.HealthApp.entities.HealthRecord;

public interface IHealthAlertMessenger {

    void sendAlert(HealthRecord record);
}
