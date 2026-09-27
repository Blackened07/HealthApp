package ru.HealthApp.service.validators;

import ru.HealthApp.dto.HealthRecordRequestDTO;

public interface IRecordValuesValidator {

    void validate(HealthRecordRequestDTO data);
}
