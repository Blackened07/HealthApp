package ru.HealthApp.service.exceptions;

public final class InvalidMetricException extends HealthAppException {
    public InvalidMetricException(String message, boolean isCritical) {
        super(message, isCritical);
    }
}
