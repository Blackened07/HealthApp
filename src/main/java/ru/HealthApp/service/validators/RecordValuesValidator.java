package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.repository.entities.HealthMetricType;
import ru.HealthApp.service.exceptions.ExceptionMessage;
import ru.HealthApp.service.exceptions.InvalidMetricException;

@Component
public class RecordValuesValidator {

    public void validate(HealthRecordRequestDTO data) {

        HealthMetricType type = HealthMetricType.fromString(data.type());

        switch (type) {
            case BLOOD_PRESSURE -> {
                validatePressure(data);
            }
            case WEIGHT -> {
                validateRange(data.value1(), 2, 300, "Масса");
            }
            case GLUCOSE -> {
                validateRange(data.value1(), 1.0, 35.0, "Сахар");
            }
            case TEMPERATURE -> {
                validateRange(data.value1(), 34.0, 42.0, "Температура");
            }
            case CUSTOM -> {
                validateCustom(data);
            }
            case null, default -> {
            }
        }

    }

    private void validatePressure(HealthRecordRequestDTO data) {

        if (data.value2() == null) {
            throw new InvalidMetricException(
                    ExceptionMessage.BP_VALUE2_ERROR.getMessage(),
                    false
            );
        }

        validateRange(data.value1(), 50.0, 250.0, "Верхнее давление");
        validateRange(data.value2(), 30.0, 150.0, "Нижнее давление");

    }

    private void validateCustom(HealthRecordRequestDTO data) {
        if (data.value1() <= 0) {
            throw new InvalidMetricException(
                    ExceptionMessage.SUB_ZERO_VALUE.getMessage(),
                    false
            );
        }

    }

    private void validateRange(Double value, double min, double max, String type) {
        if (value < min || value > max) {
            throw new InvalidMetricException(
                    ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.VALUE_OUT_OF_RANGE,
                            type,
                            min,
                            max),
                    false
            );
        }

    }

}
