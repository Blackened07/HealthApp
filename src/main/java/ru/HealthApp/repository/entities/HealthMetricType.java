package ru.HealthApp.repository.entities;


public enum HealthMetricType {

    BLOOD_PRESSURE,
    GLUCOSE,
    WEIGHT,

    CUSTOM;

    public static HealthMetricType fromString(String text) {

        for (HealthMetricType type : HealthMetricType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }

        return CUSTOM;
    }
}
