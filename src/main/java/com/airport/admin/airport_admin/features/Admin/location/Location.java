package com.airport.admin.airport_admin.features.Admin.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Entity
@Table(name = "locations")
@PreAuthorize("hasRole('Admin')")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name", nullable = false, unique = true)
    private String locationName;

    private String description;
}
