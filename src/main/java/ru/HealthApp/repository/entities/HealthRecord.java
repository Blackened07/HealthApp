package ru.HealthApp.repository.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public HealthMetricType getMetricType() {
        return HealthMetricType.fromString(this.type);
    }

    public String getUserName() {
        return user.getFirstName();
    }

    public User getAdminOfUserOfThisRecord() {
        return user.getAdmin();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        HealthRecord that = (HealthRecord) object;
        return id == that.id && Objects.equals(type, that.type) && healthMetricType == that.healthMetricType && Objects.equals(value1, that.value1) && Objects.equals(value2, that.value2) && Objects.equals(note, that.note) && Objects.equals(user, that.user) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, healthMetricType, value1, value2, note, user, timestamp);
    }
}
