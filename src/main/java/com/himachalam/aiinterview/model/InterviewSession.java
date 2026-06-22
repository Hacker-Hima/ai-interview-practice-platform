package com.himachalam.aiinterview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_session")
@Getter
@Setter
@NoArgsConstructor
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    private Double score; // 0.0 – 100.0 percentage

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String status; // IN_PROGRESS | COMPLETED

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "session_questions",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();

    // ── Computed helpers ──────────────────────────────────────────────────────
    public long getDurationMinutes() {
        if (startTime == null || endTime == null) return 0;
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    public String getPerformanceLabel() {
        if (score == null) return "N/A";
        if (score >= 90) return "Excellent";
        if (score >= 75) return "Good";
        if (score >= 60) return "Average";
        if (score >= 40) return "Below Average";
        return "Poor";
    }

    public String getPerformanceBadgeClass() {
        if (score == null) return "secondary";
        if (score >= 90) return "success";
        if (score >= 75) return "info";
        if (score >= 60) return "warning";
        return "danger";
    }
}
