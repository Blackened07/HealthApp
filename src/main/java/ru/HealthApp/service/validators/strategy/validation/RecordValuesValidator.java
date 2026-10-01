package ru.HealthApp.service.validators.strategy.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.entities.HealthMetricType;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.service.validators.strategy.alert.IHealthAlertValidator;

@Component
@RequiredArgsConstructor
class RecordValuesValidator implements IRecordValuesValidator {

    private final ValidationStrategyFactory strategyFactory;
    private final IHealthAlertValidator healthAlertValidator;

    @Override
    public void validate(HealthRecord data) {
        HealthMetricType type = HealthMetricType.fromString(data.getHealthMetricType().name());
        IMetricValidationStrategy strategy = strategyFactory.getStrategy(type);
        boolean isRecordValid = strategy.validate(data);

        if (isRecordValid) {
            healthAlertValidator.validate(data);
        }
    }
}
