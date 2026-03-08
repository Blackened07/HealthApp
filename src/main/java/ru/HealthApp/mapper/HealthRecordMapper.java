package ru.HealthApp.mapper;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.dto.HealthRecordResponseDTO;
import ru.HealthApp.repository.entities.HealthRecord;
import ru.HealthApp.repository.entities.User;

import java.time.LocalDateTime;
@Component
public class HealthRecordMapper {

    public HealthRecord toEntity(HealthRecordRequestDTO data, User target) {

        HealthRecord record = new HealthRecord();

        record.setType(data.type());
        record.setValue1(data.value1());
        record.setValue2(data.value2());
        record.setNote(data.note());

        record.setUser(target);
        record.setHealthMetricType(record.getMetricType());
        record.setTimestamp(LocalDateTime.now());

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
