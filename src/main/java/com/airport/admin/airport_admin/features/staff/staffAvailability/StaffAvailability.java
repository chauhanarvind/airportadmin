package com.airport.admin.airport_admin.features.staff.staffAvailability;

import com.airport.admin.airport_admin.features.Admin.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


// this is supposed to be for partial unavailability
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "staff_availability",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
public class StaffAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    //if unavailableFrom and unavailableTo is null and isAvailable is true,
    // that means fully available for whole day
    private LocalTime unavailableFrom;

    private LocalTime unavailableTo;

//    indicates not available for whole day
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;
}
