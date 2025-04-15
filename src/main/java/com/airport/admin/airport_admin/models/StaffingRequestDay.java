package com.airport.admin.airport_admin.models;

import com.airport.admin.airport_admin.dto.StaffingRequestItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "staffing_request_days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffingRequestDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private StaffingRequest request;

    private String Date;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffingRequestItem> items;
}
