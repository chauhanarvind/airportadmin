package com.airport.admin.airport_admin.features.staff.shiftCover;

import com.airport.admin.airport_admin.enums.CoverRequestStatus;
import com.airport.admin.airport_admin.features.Admin.user.User;
import com.airport.admin.airport_admin.features.staff.roster.RosterAssignment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "shift_cover_request",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_cover_per_shift",
                columnNames = {"shift_id"}
        )
)
public class ShiftCoverRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "original_user_id", nullable = false)
    private User originalUser;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "covering_user_id", nullable = false)
    private User coveringUser;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private RosterAssignment shift;

    @Column(name = "shift_date", nullable = false)
    private java.time.LocalDate shiftDate;

    @Column(name = "start_time", nullable = false)
    private java.time.LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private java.time.LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoverRequestStatus status = CoverRequestStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
