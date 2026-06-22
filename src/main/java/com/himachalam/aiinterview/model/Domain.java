package com.himachalam.aiinterview.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "domain")
@Getter
@Setter
@NoArgsConstructor
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String icon; // Bootstrap Icon class, e.g. "bi-cpu"
}