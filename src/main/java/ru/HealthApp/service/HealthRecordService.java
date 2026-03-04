package ru.HealthApp.service;

import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.dto.HealthRecordResponseDTO;
import ru.HealthApp.repository.entities.HealthMetricType;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthRecordService {

    HealthRecordResponseDTO createRecord(Long authorId, Long targetId, HealthRecordRequestDTO data);

    List<HealthRecordResponseDTO> getHistory(Long actorId, Long targetId, String type, LocalDateTime from, LocalDateTime to);

    List<HealthRecordResponseDTO> getFamilyDashboard(Long adminId);

    HealthRecordResponseDTO updateRecord(Long actorId, Long recordId, HealthRecordRequestDTO newData);

    List<HealthMetricType> getDefaultMetrics();
    
    void deleteRecord(Long actorId, Long recordId);
}
