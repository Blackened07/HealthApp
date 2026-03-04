package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.dto.HealthRecordResponseDTO;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.repository.HealthRecordRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.repository.entities.HealthMetricType;
import ru.HealthApp.repository.entities.HealthRecord;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.ExceptionMessage;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;
import ru.HealthApp.service.validators.AccessGuard;
import ru.HealthApp.service.validators.FamilyActionGuard;
import ru.HealthApp.service.validators.HealthAlertMessenger;
import ru.HealthApp.service.validators.RecordValuesValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthRecordServiceImpl implements HealthRecordService {

    private final UserRepository userRepository;
    private final HealthRecordRepository healthRecordRepository;

    private final HealthRecordMapper mapper;

    private final AccessGuard accessGuard;
    private final FamilyActionGuard familyActionGuard;
    private final RecordValuesValidator recordValuesValidator;
    private final HealthAlertMessenger healthAlertMessenger;


    @Override
    @Transactional
    public HealthRecordResponseDTO createRecord(Long actorId, Long targetId, HealthRecordRequestDTO data) {

        User actor = getUserFromRepository(actorId);
        User target = getUserFromRepository(targetId);

        accessGuard.checkWriteAccess(actor, target);
        recordValuesValidator.validate(data);

        HealthRecord record = mapper.mapToEntity(data,target);
        HealthRecord savedRecord = healthRecordRepository.save(record);

        healthAlertMessenger.check(savedRecord);

        return mapper.mapToResponse(savedRecord);
    }

    @Override
    public List<HealthRecordResponseDTO> getHistory(Long actorId, Long targetId, String type, LocalDateTime from, LocalDateTime to) {
        return List.of();
    }

    @Override
    public List<HealthRecordResponseDTO> getFamilyDashboard(Long adminId) {
        return List.of();
    }

    @Override
    public HealthRecordResponseDTO updateRecord(Long actorId, Long recordId, HealthRecordRequestDTO newData) {
        return null;
    }

    @Override
    public List<HealthMetricType> getDefaultMetrics() {
        return List.of();
    }

    @Override
    public void deleteRecord(Long actorId, Long recordId) {

    }

    private User getUserFromRepository(Long actorId) {
        return userRepository.findById(actorId).orElseThrow(() ->
                new ResourceNotFoundException(
                        ExceptionMessage.createMessageWithArgs(ExceptionMessage.USER_SEARCHING_ERROR, actorId),
                        false
                )
        );
    }
}
