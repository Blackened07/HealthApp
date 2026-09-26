package ru.HealthApp.service.validators;

import ru.HealthApp.dto.HealthRecordRequestDTO;

public interface RecordValuesValidatorInterface {

    void validate(HealthRecordRequestDTO data);
}
