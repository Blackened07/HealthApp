package ru.HealthApp.service.validators.strategy.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.entities.User;
import ru.HealthApp.notifications.INotification;
import ru.HealthApp.notifications.Message;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class HealthAlertNotification implements INotification {

    @Override
    public void send(Message<?> message) {

        if (message.getContent() instanceof HealthRecord) {
            sendAdminAlert((HealthRecord) message.getContent(), message.getMessage());
        }

    }

    private void sendAdminAlert(HealthRecord record, String message) {

        Optional<User> userAdmin = record.getAdminUserOfThisRecord();

        if (userAdmin.isEmpty()) {
            return;
        }

        User admin = userAdmin.get();
        User recordOwner = record.getUser();

        if (message.isBlank()) {
            return;
        }

        //создать класс Нотификейшн или Мессейдж. На клиенте создать активити с сообщениями от кого и тд
        System.err.println("!!! [ALARM] ДЛЯ: " + admin.getFirstName());
        System.err.println("У " + record.getUserName() + " обнаружено: " + message);
        System.err.println("--------------------------------------------------");
    }
}
