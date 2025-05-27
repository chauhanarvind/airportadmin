package com.airport.admin.airport_admin.features.shiftCover;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShiftCoverRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User originalUser;

    @ManyToOne
    private User coveringUser;

    private LocalDate shiftDate;

    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private CoverRequestStatus status = CoverRequestStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String warnings; // Optional JSON or plain string

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters (or use Lombok)
}
