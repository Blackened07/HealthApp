package ru.HealthApp.service.validators.strategy.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.entities.User;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class HealthAlertMessenger implements IHealthAlertMessenger {

    private final HealthAlertValidator healthAlertValidator;

    public void sendAlert(HealthRecord record) {
        AlertMessage message = healthAlertValidator.validate(record);
        sendAdminAlert(record, message);
    }

    private void sendAdminAlert(HealthRecord record, AlertMessage message) {

        Optional<User> userAdmin = record.getAdminUserOfThisRecord();

        if (userAdmin.isEmpty()) {
            return;
        }

        User admin = userAdmin.get();
        String reason = message.getMessage();

        if (reason.isBlank()) {
            return;
        }

        //создать класс Нотификейшн или Мессейдж. На клиенте создать активити с сообщениями от кого и тд
        System.err.println("!!! [ALARM] ДЛЯ: " + admin.getFirstName());
        System.err.println("У " + record.getUserName() + " обнаружено: " + reason);
        System.err.println("--------------------------------------------------");
    }

}
