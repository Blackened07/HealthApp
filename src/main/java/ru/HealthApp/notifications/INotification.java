package ru.HealthApp.notifications;


public interface INotification {
    void send(Message<?> message);
}
