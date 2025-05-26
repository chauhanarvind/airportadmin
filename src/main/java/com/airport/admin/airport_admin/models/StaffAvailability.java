package com.airport.admin.airport_admin.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "staff_availability")
@Getter
@Setter
@NoArgsConstructor
public class StaffAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime unavailableFrom;

    private LocalTime unavailableTo;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
}
