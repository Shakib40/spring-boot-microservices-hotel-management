package com.auth.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blocked_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class blocked {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private String id;

    private String userName;

    @Enumerated(EnumType.STRING)
    private BlockReason reason;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp blockedAt;

    @Column(name = "blocked_until", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP + INTERVAL '20 minutes'")
    private Timestamp blockedUntil;

    @PrePersist
    public void setBlockTimestamps() {
        if (blockedAt == null) {
            blockedAt = Timestamp.valueOf(LocalDateTime.now());
        }
        if (blockedUntil == null) {
            blockedUntil = Timestamp.valueOf(LocalDateTime.now().plusMinutes(20));
        }
    }
}