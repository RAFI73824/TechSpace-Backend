package com.techspace.course_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User receiving notification
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Course related notification
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;

    @Column(length = 1000)
    private String message;

    private String type;

    private String severity;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}