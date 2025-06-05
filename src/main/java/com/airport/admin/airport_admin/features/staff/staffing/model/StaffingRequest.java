package com.airport.admin.airport_admin.features.staff.staffing.model;
import com.airport.admin.airport_admin.enums.RequestType;
import com.airport.admin.airport_admin.enums.RosterStatus;
import com.airport.admin.airport_admin.features.Admin.location.Location;
import com.airport.admin.airport_admin.features.Admin.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;


// Each Staffing request has multiple days...since each manager makes a weekly roster
@Entity
@Table(name = "staffing_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="manager_id", nullable = false)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestType requestType = RequestType.REGULAR;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RosterStatus status  = RosterStatus.PENDING;

    private String reason;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffingRequestDay> days;

    @Column(name = "unassigned_shift_count", nullable = false)
    private int unassignedShiftCount = 0;

}
