package ru.HealthApp.service.exceptions;

public final class IllegalActionException extends HealthAppException {
    public IllegalActionException(String message) {
        super(message, false);
    }
}
