package ru.HealthApp.repository.entities;

import jakarta.persistence.*;
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
public class Doctor extends Account {
    // Специальные поля типа специализация, лицензия и тд

    @ManyToMany(mappedBy = "doctors")
    private List<Family> families = new ArrayList<>();

    public void setFamily (Family family) {
        if (!families.contains(family)) {
            families.add(family);
        }
    }

}
