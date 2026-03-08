package ru.HealthApp.service.exceptions;

public final class IllegalActionException extends HealthAppException {
    public IllegalActionException(String message, boolean isCritical) {
        super(message, isCritical);
    }
}
