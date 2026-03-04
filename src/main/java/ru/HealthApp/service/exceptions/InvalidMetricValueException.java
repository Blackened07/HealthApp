package ru.HealthApp.service.exceptions;

public class InvalidMetricValueException extends HealthAppException {
    public InvalidMetricValueException(String message, boolean isCritical) {
        super(message, isCritical);
    }
}

