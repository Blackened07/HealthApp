package ru.HealthApp.service.validators.strategy.validation;

import ru.HealthApp.dto.HealthRecordRequestDTO;

public interface IRecordValuesValidator {

    void validate(HealthRecordRequestDTO data);
}
