package ru.HealthApp.service.validators;

import ru.HealthApp.entities.HealthRecord;

public interface IHealthAlertMessenger {

    void check(HealthRecord record);
}
