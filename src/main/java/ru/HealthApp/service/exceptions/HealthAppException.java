package ru.HealthApp.service.exceptions;

import lombok.Getter;

@Getter
public abstract class HealthAppException extends RuntimeException {
    /// true - пишем в логи
    private final boolean critical;

    public HealthAppException(String message, boolean isCritical) {
        super(message);
        this.critical = isCritical;
    }


}
