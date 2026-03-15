package ru.HealthApp.repository.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Почти не может быть пустой")
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;

    @ManyToMany(mappedBy = "doctors")
    private List<Family> families = new ArrayList<>();

    public void setFamily (Family family) {
        if (!families.contains(family)) {
            families.add(family);
        }
    }

}
