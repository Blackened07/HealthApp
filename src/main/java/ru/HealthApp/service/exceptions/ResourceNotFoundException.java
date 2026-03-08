package ru.HealthApp.service.exceptions;

public final class ResourceNotFoundException extends HealthAppException {
    public ResourceNotFoundException(String message, boolean isCritical) {
        super(message, isCritical);
    }

}
