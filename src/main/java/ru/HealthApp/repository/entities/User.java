package ru.HealthApp.repository.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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
    @NotNull(message = "Почти не может быть пустой")
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FamilyRole familyRole = FamilyRole.USER;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(nullable = false)
    private LocalDateTime lastActivity;

    public User getAdmin() {
        return family != null ? family.findAdmin() : null;
    }

    public boolean isAdmin() {
        return familyRole == FamilyRole.ADMIN;
    }

    public boolean isDoctor() {
        return familyRole == FamilyRole.DOCTOR;
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

    public boolean isDoctorOfUserFamily(User doctor) {

        if (isNoFamily()) {
            return false;
        }
        return family.isFamilyDoctor(doctor);
    }

    @Override
    public boolean equals(Object object) {

        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstName, user.firstName) && familyRole == user.familyRole && Objects.equals(family, user.family) && Objects.equals(lastActivity, user.lastActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, familyRole, family, lastActivity);
    }
}
