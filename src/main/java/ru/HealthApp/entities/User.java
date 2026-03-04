package ru.HealthApp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    /// Поле isVirtual определяет является ли пользователь активным юзером (false) или прикреплённой учёткой к админу семьи(true)
    private boolean isVirtual;
}
