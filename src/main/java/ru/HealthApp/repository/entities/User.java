package ru.HealthApp.repository.entities;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;

    @Enumerated(EnumType.STRING)
    private FamilyRole familyRole;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(nullable = false)
    private LocalDateTime lastActivity;

    public User getAdmin() {
        return family.findAdmin();
    }

    /**
     * Поле isVirtual определяет является ли пользователь активным юзером (false)
     * или прикреплённой учёткой к админу семьи(true)
     */
    private boolean isVirtual;

    public boolean isAdmin() {
        return familyRole == FamilyRole.ADMIN;
    }

    public boolean isDoctor() {
        return familyRole == FamilyRole.DOCTOR;
    }

    public boolean isMember() {
        return familyRole == FamilyRole.MEMBER;
    }

    public boolean isVirtual() {
        return familyRole == FamilyRole.VIRTUAL;
    }

    public boolean isNoFamily() {
        return family == null;
    }

    public boolean isDoctorOfUserFamily(User doctor) {
        return family.isFamilyDoctor(doctor);
    }

    // Специфичные методы для разных ролей

    /**
     * Может ли пользователь управлять семьей (добавлять/удалять членов)
     */
    public boolean canManageFamily() {
        return isAdmin();
    }

    /**
     * Может ли пользователь записывать медицинские данные
     */
    public boolean canWriteMedicalRecords() {
        return !isDoctor(); // Врачи могут только читать
    }

    /**
     * Может ли пользователь просматривать семейную панель
     */
    public boolean canAccessFamilyDashboard() {
        return isAdmin();
    }

    /**
     * Может ли пользователь просматривать медицинские записи (для врачей)
     */
    public boolean canAccessMedicalRecords() {
        return isDoctor();
    }

    /**
     * Может ли данный пользователь управлять этим пользователем
     */
    public boolean canBeManagedBy(User manager) {
        return isVirtual() && manager.isAdmin() && manager.getFamily().equals(getFamily());
    }

    /**
     * Может ли данный пользователь записывать данные для этого пользователя
     */
    public boolean canBeWrittenBy(User writer) {
        if (writer.equals(this)) return true; // Себе всегда можно
        if (isVirtual() && writer.isAdmin() && writer.getFamily().equals(getFamily())) return true;
        return writer.getFamily().equals(getFamily()) && writer.isAdmin();
    }

    /**
     * Может ли данный пользователь читать данные этого пользователя
     */
    public boolean canBeReadBy(User reader) {
        if (reader.equals(this)) return true; // Себе всегда можно

        // Врач может читать если он врач этой семьи
        if (reader.isDoctor() && isDoctorOfUserFamily(reader)) {
            return true;
        }

        // Члены одной семьи могут читать друг у друга
        return !reader.isNoFamily() && reader.getFamily().equals(getFamily());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return isVirtual == user.isVirtual && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && familyRole == user.familyRole && Objects.equals(family, user.family) && Objects.equals(lastActivity, user.lastActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, familyRole, family, lastActivity, isVirtual);
    }
}
