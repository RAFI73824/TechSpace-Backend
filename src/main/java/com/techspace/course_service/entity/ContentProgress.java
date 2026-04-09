package com.techspace.course_service.entity;
import jakarta.persistence.PrePersist;
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
@Table(name = "content_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 USER
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 🔹 CONTENT
    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    // 🔥 RESUME FEATURE (IMPORTANT)
    private double lastWatchedSeconds;

    // 🔥 VIDEO TOTAL DURATION (optional but powerful)
    private double duration;

    // 🔥 COMPLETION FLAG
    private boolean completed;

    // 🔥 TRACK LAST ACTIVITY
    private java.time.LocalDateTime updatedAt;
    
  

}