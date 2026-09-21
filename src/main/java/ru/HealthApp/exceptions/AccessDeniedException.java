package ru.HealthApp.exceptions;

public final class AccessDeniedException extends HealthAppException {

    public AccessDeniedException(String message) {
        super(message, true);
    }

}
