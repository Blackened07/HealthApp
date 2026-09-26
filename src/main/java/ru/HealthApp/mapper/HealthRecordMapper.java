package ru.HealthApp.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.dto.HealthRecordResponseDTO;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.entities.User;
import ru.HealthApp.mapper.util.DateTimeConverter;

@Component
@RequiredArgsConstructor
public class HealthRecordMapper {

    private final DateTimeConverter dateTimeConverter;

    public HealthRecord toEntity(HealthRecordRequestDTO data, User target) {
        HealthRecord record = new HealthRecord();

        record.setType(data.type());
        record.setValue1(data.value1());
        record.setValue2(data.value2());
        record.setNote(data.note());
        record.setUser(target);
        record.setHealthMetricType(record.getMetricType());
        record.setTimestamp(dateTimeConverter.convertMillisToLocalDateTime(
                data.TIMESTAMP(),
                data.OFFSET_ZONE()
        ));

        return record;
    }

    public HealthRecordResponseDTO toResponse(HealthRecord record) {
        return new HealthRecordResponseDTO(
                record.getId(),
                record.getType(),
                record.getValue1(),
                record.getValue2(),
                record.getNote(),
                record.getTimestamp(),
                record.getUserName()
        );
    }
}
