package com.airport.admin.airport_admin.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Table(name="users")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="email", unique = true, nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "job_level_id", nullable = false)
    private JobLevel jobLevel;

    @ManyToOne
    @JoinColumn(name = "job_role_id", nullable = false)
    private JobRole jobRole;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constraint_profile_id")
    private ConstraintProfile constraintProfile;
}
