package ru.HealthApp.service.validators.strategy.alert;

import lombok.Getter;

@Getter
public class AlertMessage {
    private final String message;

    private AlertMessage(String message) {
        this.message = message;
    }

    public static AlertMessage create(String message) {
        return new AlertMessage(message);
    }

}
