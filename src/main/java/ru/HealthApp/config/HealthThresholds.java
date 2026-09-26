package ru.HealthApp.config;

import org.springframework.stereotype.Component;

@Component
public class HealthThresholds {

    // Blood Pressure thresholds
    public static final double PRESSURE_SYS_MAX = 170.0;
    public static final double PRESSURE_SYS_MIN = 80.0;
    public static final double PRESSURE_DIA_MAX = 110.0;
    public static final double PRESSURE_DIA_MIN = 40.0;

    // Glucose thresholds
    public static final double GLUCOSE_MAX = 13.0;
    public static final double GLUCOSE_MIN = 3.5;

    // Temperature thresholds
    public static final double TEMPERATURE_MAX = 39.0;
    public static final double TEMPERATURE_MIN = 35.0;

    // Validation ranges
    public static final double WEIGHT_MIN = 2.0;
    public static final double WEIGHT_MAX = 300.0;

    public static final double GLUCOSE_VALID_MIN = 1.0;
    public static final double GLUCOSE_VALID_MAX = 35.0;

    public static final double TEMPERATURE_VALID_MIN = 34.0;
    public static final double TEMPERATURE_VALID_MAX = 42.0;

    public static final double PRESSURE_SYS_VALID_MIN = 50.0;
    public static final double PRESSURE_SYS_VALID_MAX = 250.0;
    public static final double PRESSURE_DIA_VALID_MIN = 30.0;
    public static final double PRESSURE_DIA_VALID_MAX = 150.0;
}
