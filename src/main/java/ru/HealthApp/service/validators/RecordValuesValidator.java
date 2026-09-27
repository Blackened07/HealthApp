package ru.HealthApp.service.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.entities.HealthMetricType;
import ru.HealthApp.service.validators.strategy.validation.IMetricValidationStrategy;
import ru.HealthApp.service.validators.strategy.validation.ValidationStrategyFactory;

@Component
@RequiredArgsConstructor
public class RecordValuesValidator implements IRecordValuesValidator {

    private final ValidationStrategyFactory strategyFactory;

    @Override
    public void validate(HealthRecordRequestDTO data) {
        HealthMetricType type = HealthMetricType.fromString(data.type());
        IMetricValidationStrategy strategy = strategyFactory.getStrategy(type);
        strategy.validate(data);
    }
}
