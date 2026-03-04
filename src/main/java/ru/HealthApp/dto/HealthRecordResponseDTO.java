package ru.HealthApp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthRecordResponseDTO {
    private Long id;
    private String type;
    private Double value1;
    private Double value2;
    private String note;
    private LocalDateTime timestamp;
    private String userName;
}
