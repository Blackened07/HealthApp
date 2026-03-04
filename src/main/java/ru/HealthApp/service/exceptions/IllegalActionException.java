package ru.HealthApp.service.exceptions;

public class IllegalActionException extends HealthAppException {
    public IllegalActionException(String message, boolean isCritical) {
        super(message, isCritical);
    }
}
