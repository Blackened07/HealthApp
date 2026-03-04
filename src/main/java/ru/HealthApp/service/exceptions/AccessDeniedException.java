package ru.HealthApp.service.exceptions;

public class AccessDeniedException extends HealthAppException {

    public AccessDeniedException(String message, boolean isCritical) {
        super(message, isCritical);
    }
}
