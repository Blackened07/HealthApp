package ru.HealthApp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
@Getter
@Setter
@NoArgsConstructor
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String type;

    private HealthMetricType healthMetricType;

    @Column(nullable = false)
    private Double value1;
    private Double value2;

    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private LocalDateTime lastActivity;

    public HealthMetricType getMetricType() {
        return HealthMetricType.fromString(this.type);
    }

}
