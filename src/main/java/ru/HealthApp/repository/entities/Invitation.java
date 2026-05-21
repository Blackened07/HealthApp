package ru.HealthApp.repository.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "family_invitation")
@Getter
@Setter
@NoArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String actorEmail;

    @Column(nullable = false)
    private String secretCode;

    @Column(nullable = false)
    private String invitedUserEmail;

    @Column
    private String familyName;
    //TODO: create enum with statuses: PENDING, USED, EXPIRED
    @Column(nullable = false)
    private String status;

    @Column
    private LocalDateTime createdAtTimestamp;
    @Column
    private LocalDateTime expirationTimestamp;



}
