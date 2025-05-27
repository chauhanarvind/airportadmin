package com.airport.admin.airport_admin.features.jobRole;

import com.airport.admin.airport_admin.features.jobCategory.JobCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Entity
@Table(name = "job_roles")
public class JobRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role_name",  nullable = false, unique = true)
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private JobCategory category;


}
