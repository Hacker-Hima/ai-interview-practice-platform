package com.himachalam.aiinterview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String userAnswer;

    private Integer score; // 0-100

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    @Column(columnDefinition = "TEXT")
    private String strengths; // semicolon-separated list

    @Column(columnDefinition = "TEXT")
    private String weaknesses; // semicolon-separated list

    @Column(columnDefinition = "TEXT")
    private String recommendedTopics; // semicolon-separated list

    private Boolean evaluatedByAI = false;

    // ── Computed helpers for Thymeleaf ────────────────────────────────────────
    public String[] getStrengthList() {
        return strengths != null && !strengths.isBlank()
            ? strengths.split(";") : new String[0];
    }

    public String[] getWeaknessList() {
        return weaknesses != null && !weaknesses.isBlank()
            ? weaknesses.split(";") : new String[0];
    }

    public String[] getRecommendedTopicList() {
        return recommendedTopics != null && !recommendedTopics.isBlank()
            ? recommendedTopics.split(";") : new String[0];
    }

    public String getScoreBadgeClass() {
        if (score == null) return "secondary";
        if (score >= 80) return "success";
        if (score >= 60) return "warning";
        return "danger";
    }
}
