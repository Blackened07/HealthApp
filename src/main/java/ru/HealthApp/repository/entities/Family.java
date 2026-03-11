package ru.HealthApp.repository.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "families")
@Getter
@Setter
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL)
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "family_doctors",
            joinColumns = @JoinColumn(name = "family_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private List<User> doctors;

    public boolean isFamilyDoctor(User doctor) {
        return doctors.contains(doctor);
    }

    public User findAdmin() {
        for (User user : users) {
            if (user.getFamilyRole() == FamilyRole.ADMIN) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Family family = (Family) object;
        return id == family.id && Objects.equals(name, family.name) && Objects.equals(users, family.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, users);
    }
}
