package ru.HealthApp.service.validators.strategy.validation;

import ru.HealthApp.entities.HealthRecord;

public interface IRecordValuesValidator {

    void validate(HealthRecord data);
}
