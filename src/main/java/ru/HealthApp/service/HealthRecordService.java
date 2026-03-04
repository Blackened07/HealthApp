package ru.HealthApp.service;

public interface HealthRecordService {

    // Создание записи (за себя или за вирт)
    HealthRecordResponseDTO createRecord(Long authorId, Long targetId, HealthRecordRequestDTO data);

    // 2. Чтение истории за период (с фильтром по типу)
    List<HealthRecordResponseDTO> getHistory(Long actorId, Long targetId, String type, LocalDateTime from, LocalDateTime to);

    // 3. Сводка по семье (имя + дата + последний замер каждого участника)
    List<HealthRecordResponseDTO> getFamilyDashboard(Long adminId);

    // 4. Редактирование записи
    HealthRecordResponseDTO updateRecord(Long actorId, Long recordId, HealthRecordRequestDTO newData);

    // 5. Список всех типов по умолчанию (для UI)
    List<HealthMetricType> getDefaultMetrics();

    // 6. Удаление записи
    void deleteRecord(Long actorId, Long recordId);
}
