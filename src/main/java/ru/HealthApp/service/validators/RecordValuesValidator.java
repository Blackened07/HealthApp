package ru.HealthApp.service.validators;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.repository.entities.HealthMetricType;
import ru.HealthApp.service.exceptions.ExceptionMessage;
import ru.HealthApp.service.exceptions.InvalidMetricValueException;

@Component
public class RecordValuesValidator {

    public void validate(HealthRecordRequestDTO data) {

        if (data.getValue1() == null) {
            throwClassException(ExceptionMessage.MAIN_VALUE_ERROR.getMessage());
        }

        HealthMetricType type = HealthMetricType.fromString(data.getType());

        switch (type) {
            case BLOOD_PRESSURE -> {
                validatePressure(data);
            }
            case WEIGHT -> {
                validateRange(data.getValue1(), 2, 300, "Масса");}
            case GLUCOSE -> {
                validateRange(data.getValue1(), 1.0, 35.0, "Сахар");}
            case TEMPERATURE -> {
                validateRange(data.getValue1(), 34.0, 42.0, "Температура");
            }
            case CUSTOM -> {
                validateCustom(data);
            }
            case null, default -> {}
        }

    }

    private void validateRange(Double value, double min, double max, String type) {
        if (value < min || value > max) {
            throwClassException(
                    ExceptionMessage.createMessageWithArgs(
                            ExceptionMessage.VALUE_OUT_OF_RANGE,
                            type,
                            min,
                            max)
            );
        }
    }

    private void validateCustom(HealthRecordRequestDTO data) {
        if (data.getValue1() <= 0) {
            throwClassException(ExceptionMessage.SUB_ZERO_VALUE.getMessage());
        }
    }

    private void validatePressure(HealthRecordRequestDTO data) {
        if (data.getValue2() == null) {
            throwClassException(ExceptionMessage.BP_VALUE2_ERROR.getMessage());
        }
        validateRange(data.getValue1(), 50.0, 250.0, "Верхнее давление");
        validateRange(data.getValue2(), 30.0, 150.0, "Нижнее давление");

    }

    private void throwClassException(String message) {
        throw new InvalidMetricValueException(message, false);
    }

}
