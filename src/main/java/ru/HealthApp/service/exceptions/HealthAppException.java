package ru.HealthApp.service.exceptions;

import lombok.Getter;

@Getter
public sealed abstract class HealthAppException
        extends RuntimeException
        permits AccessDeniedException, IllegalActionException, InvalidMetricException, ResourceNotFoundException {

    private final boolean critical;

    public HealthAppException(String message, boolean isCritical) {
        super(message);
        this.critical = isCritical;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
