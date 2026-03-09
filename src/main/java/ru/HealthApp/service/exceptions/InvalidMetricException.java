package ru.HealthApp.service.exceptions;

public final class InvalidMetricException extends HealthAppException {
    public InvalidMetricException(String message) {
        super(message, false);
    }
    
    public static InvalidMetricException outOfRange(String type, double min, double max) {
        return new InvalidMetricException(
            ExceptionMessage.createMessageWithArgs(
                ExceptionMessage.VALUE_OUT_OF_RANGE,
                type,
                min,
                max
            )
        );
    }
    
    public static InvalidMetricException subZeroValue() {
        return new InvalidMetricException(
            ExceptionMessage.SUB_ZERO_VALUE.getMessage()
        );
    }
    
    public static InvalidMetricException pressureValue2Required() {
        return new InvalidMetricException(
            ExceptionMessage.BP_VALUE2_ERROR.getMessage()
        );
    }
}
