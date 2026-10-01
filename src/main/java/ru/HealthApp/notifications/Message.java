package ru.HealthApp.notifications;

import lombok.Getter;

@Getter
public class Message<T> {
    private final String message;
    private final T content;

    private Message(String message, T content) {
        this.message = message;
        this.content = content;
    }

    public static <T> Message<T> create(String message, T content) {
        return new Message<>(message, content);
    }

}
