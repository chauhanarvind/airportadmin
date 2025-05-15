package com.airport.admin.airport_admin.models;

import com.airport.admin.airport_admin.enums.SwapStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftSwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Person requesting the swap
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // Person they want to swap with
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_user_id", nullable = false)
    private User requestedUser;

    private LocalDate shiftDate;

    private String reason;

    @Enumerated(EnumType.STRING)
    private SwapStatus status = SwapStatus.PENDING;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
