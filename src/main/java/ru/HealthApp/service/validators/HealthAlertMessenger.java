package ru.HealthApp.service.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.entities.User;
import ru.HealthApp.service.validators.strategy.alert.AlertMessage;
import ru.HealthApp.service.validators.strategy.alert.HealthAlertFactory;
import ru.HealthApp.service.validators.strategy.alert.IHealthAlertStrategy;


@Component
@RequiredArgsConstructor
public class HealthAlertMessenger implements IHealthAlertMessenger {

    private final HealthAlertFactory factory;

    public void check(HealthRecord record) {
        String alertReason = "";

        IHealthAlertStrategy strategy = factory.getStrategy(record.getMetricType());

        AlertMessage alertMessage = strategy.check(record, alertReason);
        sendAdminAlert(record, alertMessage);
    }

    private void sendAdminAlert(HealthRecord record, AlertMessage message) {
        User userAdmin = record.getAdminUserOfThisRecord();
        String reason = message.getMessage();
        if (reason.isBlank()) {
            return;
        }

        if (userAdmin == null) {
            return;
        }

        System.err.println("!!! [ALARM] ДЛЯ: " + userAdmin.getFirstName());
        System.err.println("У " + record.getUserName() + " обнаружено: " + reason);
        System.err.println("--------------------------------------------------");
    }

}
