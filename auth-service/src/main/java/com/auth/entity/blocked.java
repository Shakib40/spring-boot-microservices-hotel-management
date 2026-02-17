package com.auth.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blocked_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class blocked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String reason; // MULTIPLE_FAILED_OTP_ATTEMPTS, ADMIN_ACTION
    private Timestamp blockedAt;
    private Timestamp blockedUntil;
}
