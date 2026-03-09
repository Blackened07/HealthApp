package ru.HealthApp.service.exceptions;

public final class AccessDeniedException extends HealthAppException {

    public AccessDeniedException(String message) {
        super(message, true);
    }

}
