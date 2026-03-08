package ru.HealthApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.dto.HealthRecordResponseDTO;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.repository.HealthRecordRepository;
import ru.HealthApp.repository.UserRepository;
import ru.HealthApp.repository.entities.Family;
import ru.HealthApp.repository.entities.HealthMetricType;
import ru.HealthApp.repository.entities.HealthRecord;
import ru.HealthApp.repository.entities.User;
import ru.HealthApp.service.exceptions.ExceptionMessage;
import ru.HealthApp.service.exceptions.IllegalActionException;
import ru.HealthApp.service.exceptions.ResourceNotFoundException;
import ru.HealthApp.service.validators.AccessGuard;
import ru.HealthApp.service.validators.FamilyActionGuard;
import ru.HealthApp.service.validators.HealthAlertMessenger;
import ru.HealthApp.service.validators.RecordValuesValidator;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        HealthRecord record = mapper.toEntity(data, target);
        HealthRecord savedRecord = healthRecordRepository.save(record);

        healthAlertMessenger.check(savedRecord);

        return mapper.toResponse(savedRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HealthRecordResponseDTO> getHistory(Long actorId, Long targetId, String type, LocalDateTime from, LocalDateTime to) {
        User actor = getUserFromRepository(actorId);
        User target = getUserFromRepository(targetId);

        accessGuard.checkReadAccess(actor, target);

        List<HealthRecord> records = (type == null)
                ? healthRecordRepository.findAllByUserAndTimestampBetween(target, from, to)
                : healthRecordRepository.findAllByUserAndTypeOrderByTimestampDesc(target, type.toUpperCase(), from, to);

        return records.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HealthRecordResponseDTO> getFamilyDashboard(Long actorId) {

        User actor = getUserFromRepository(actorId);

        accessGuard.checkFamilyDashboardAccess(actor);

        Family family = actor.getFamily();

        return family.getUsers().stream()
                .map(healthRecordRepository::findFirstByUserOrderByTimestampDesc)
                .flatMap(Optional::stream)
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public HealthRecordResponseDTO updateRecord(Long actorId, Long recordId, HealthRecordRequestDTO newData) {

        User actor = getUserFromRepository(actorId);
        HealthRecord record = getRecordFromRepository(recordId);

        accessGuard.checkWriteAccess(actor, actor);
        recordValuesValidator.validate(newData);

        update(record, newData);

        HealthRecord updated = healthRecordRepository.save(record);

        return mapper.toResponse(updated);
    }

    @Override
    public List<HealthMetricType> getDefaultMetrics() {
        return Arrays.asList(HealthMetricType.values());
    }

    @Override
    public void deleteRecord(Long actorId, Long recordId) {
        User actor = getUserFromRepository(actorId);
        HealthRecord record = getRecordFromRepository(recordId);

        accessGuard.checkWriteAccess(actor, record.getUser());

        healthRecordRepository.delete(record);
    }

    private User getUserFromRepository(Long actorId) {
        return userRepository.findById(actorId).orElseThrow(() ->
                new ResourceNotFoundException(
                        ExceptionMessage.createMessageWithArgs(ExceptionMessage.USER_SEARCHING_ERROR, actorId),
                        false
                )
        );
    }

    private HealthRecord getRecordFromRepository(Long recordId) {
        return healthRecordRepository.findById(recordId).orElseThrow(() ->
                new ResourceNotFoundException(
                        ExceptionMessage.createMessageWithArgs(ExceptionMessage.RECORD_NOT_FOUND, ""),
                        false
                )
        );
    }

    private void update(HealthRecord record, HealthRecordRequestDTO newData) {

        if (newData instanceof HealthRecordRequestDTO(String type, Double v1, Double v2, String note)) {

            if (type.equalsIgnoreCase(record.getType())) {
                record.setValue1(v1);
                record.setValue2(v2);
                record.setNote(note);
            } else {
                throw new IllegalActionException("Разные типы! Невозможно отредактировать запись!", false);
            }

        }
    }
}
