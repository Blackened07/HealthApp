package ru.HealthApp.service.validators.strategy.alert;

import ru.HealthApp.entities.HealthRecord;

public interface IHealthAlertValidator {
    void validate(HealthRecord record);
}
