package ru.HealthApp.service.validators.strategy.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthRecord;

@Component
@RequiredArgsConstructor
class HealthAlertValidator {

    private final HealthAlertFactory factory;

    AlertMessage validate(HealthRecord record) {
        String reason = "";
        IHealthAlertStrategy strategy = factory.getStrategy(record.getMetricType());

        return strategy.getAlertMessage(record, reason);
    }
}
