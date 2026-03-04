package ru.HealthApp.dto;

import lombok.Data;

@Data
public class HealthRecordRequestDTO {

    private String type;
    private Double value1;
    private Double value2;
    private String note;

}
