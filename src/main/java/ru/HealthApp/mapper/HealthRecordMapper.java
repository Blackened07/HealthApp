package ru.HealthApp.mapper;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.dto.HealthRecordResponseDTO;
import ru.HealthApp.repository.entities.HealthRecord;
import ru.HealthApp.repository.entities.User;

import java.time.LocalDateTime;
@Component
public class HealthRecordMapper {

    public HealthRecord mapToEntity(HealthRecordRequestDTO data, User target) {

        HealthRecord record = new HealthRecord();

        record.setType(data.getType());
        record.setValue1(data.getValue1());
        record.setValue2(data.getValue2());
        record.setNote(data.getNote());

        record.setUser(target);
        record.setHealthMetricType(record.getMetricType());
        record.setTimestamp(LocalDateTime.now());

        return record;
    }

    public HealthRecordResponseDTO mapToResponse(HealthRecord record) {
        HealthRecordResponseDTO responseDTO = new HealthRecordResponseDTO();

        responseDTO.setId(record.getId());
        responseDTO.setType(record.getType());
        responseDTO.setValue1(record.getValue1());
        responseDTO.setValue2(record.getValue2());
        responseDTO.setNote(record.getNote());
        responseDTO.setTimestamp(record.getTimestamp());
        responseDTO.setUserName(record.getUserName());

        return responseDTO;
    }
}
