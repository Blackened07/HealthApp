package ru.HealthApp.repository.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("DOCTOR")
public final class User extends Account{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FamilyRole familyRole = FamilyRole.NO_FAMILY_USER;

    @ManyToOne
    @JoinColumn(name = "family_id")
    @JsonIgnore
    private Family family;

    @Column(nullable = false)
    private LocalDateTime lastActivity;

    public User getAdmin() {
        return family != null ? family.findAdmin() : null;
    }

    public boolean isAdmin() {
        return familyRole == FamilyRole.ADMIN;
    }

    public boolean isVirtual() {
        return familyRole == FamilyRole.VIRTUAL;
    }

    public boolean isMember() {
        return familyRole == FamilyRole.MEMBER;
    }

    public boolean isNoFamily() {

        return family == null;
    }

    public boolean isDoctorOfUserFamily(Doctor doctor) {

        if (isNoFamily()) {
            return false;
        }
        return family.isFamilyDoctor(doctor);
    }

    @Override
    public SystemRole getRole() {
        return SystemRole.USER;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return familyRole == user.familyRole && Objects.equals(lastActivity, user.lastActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(familyRole, family, lastActivity);
    }
}
