package com.himachalam.aiinterview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String expectedAnswer;

    @Column(nullable = false)
    private String difficulty; // EASY, MEDIUM, HARD

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;
}