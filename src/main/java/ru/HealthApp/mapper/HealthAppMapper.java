package ru.HealthApp.mapper;

import org.springframework.stereotype.Component;
import ru.HealthApp.dto.*;
import ru.HealthApp.entities.Doctor;
import ru.HealthApp.entities.Family;
import ru.HealthApp.entities.HealthRecord;
import ru.HealthApp.entities.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class HealthAppMapper {

    public HealthRecord toEntity(HealthRecordRequestDTO data, User target) {

        HealthRecord record = new HealthRecord();

        int deviceOffset = data.OFFSET_ZONE();
        long createdAt = data.TIMESTAMP();

//        ZoneOffset offsetInSeconds =  ZoneOffset.ofTotalSeconds(deviceOffset / 1000);
        int offsetInSeconds = Math.toIntExact(java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(deviceOffset));
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetInSeconds);
        Instant instant = Instant.ofEpochMilli(createdAt);

        record.setType(data.type());
        record.setValue1(data.value1());
        record.setValue2(data.value2());
        record.setNote(data.note());


        record.setUser(target);
        record.setHealthMetricType(record.getMetricType());
        record.setTimestamp(LocalDateTime.ofInstant(instant, offset));

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

    public UserResponseDTO toResponse(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getFamilyRole(),
                user.getLastActivity()
        );
    }

    public FamilyResponseDTO toResponse(Family family, String familyRole) {

        return new FamilyResponseDTO(
                family.getId(),
                family.getName(),
                familyRole
        );
    }


    public DoctorResponseDTO toResponse(Doctor doctor) {

        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getFirstName()
        );
    }

}
