package ru.HealthApp.service.validators.strategy.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.entities.HealthMetricType;

@Component
@RequiredArgsConstructor
class RecordValuesValidator implements IRecordValuesValidator {

    private final ValidationStrategyFactory strategyFactory;

    @Override
    public void validate(HealthRecordRequestDTO data) {
        HealthMetricType type = HealthMetricType.fromString(data.type());
        IMetricValidationStrategy strategy = strategyFactory.getStrategy(type);
        strategy.validate(data);
    }
}
