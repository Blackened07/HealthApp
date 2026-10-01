package ru.HealthApp.service.validators.strategy.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.notifications.INotification;
import ru.HealthApp.notifications.Message;

@Component
@RequiredArgsConstructor
public class HealthAlertValidator implements IHealthAlertValidator{

    private final HealthAlertFactory factory;
    private final INotification notification;

    @Override
    public void validate(HealthRecord record) {
        String reason = "";
        IHealthAlertStrategy strategy = factory.getStrategy(record.getMetricType());
        Message<HealthRecord> message = strategy.getAlertMessage(record, reason);
        notification.send(message);
    }

}
