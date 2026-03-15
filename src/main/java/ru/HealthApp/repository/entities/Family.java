package ru.HealthApp.repository.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "family_doctors",
            joinColumns = @JoinColumn(name = "family_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private List<Doctor> doctors = new ArrayList<>();

    public boolean isFamilyDoctor(Doctor doctor) {
        return doctors.contains(doctor);
    }

    //add Doctor

    /**
     * Семья добавляет доктора - доктору приходит уведомление.
     * В семью добавляется доктор, у доктора после уведомления срабатывает его метод аддФэмили
     *
     */
    public void addDoctor(Doctor doctor) {
        if (!doctors.contains(doctor)) {
            doctors.add(doctor);
        }
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            user.setFamily(this);
        }
    }

    public void removeUser(User user) {
        if (users.remove(user)) {
            user.setFamily(null);
        }
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
        return id == family.id && Objects.equals(name, family.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, users);
    }
}
