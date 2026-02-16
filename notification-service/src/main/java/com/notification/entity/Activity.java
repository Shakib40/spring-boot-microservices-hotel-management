package com.notification.entity;

import com.notification.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    private LocalDateTime createdAt;
    private String createdBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}