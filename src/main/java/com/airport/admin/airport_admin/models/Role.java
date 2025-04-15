package com.airport.admin.airport_admin.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="roles")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String name;
}
