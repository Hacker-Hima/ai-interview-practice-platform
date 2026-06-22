package com.himachalam.aiinterview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Holds the result of evaluating one answer — produced by EvaluationService.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResult {

    private int score; // 0-100
    private String feedback;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> recommendedTopics;
    private boolean aiEvaluated; // true if Gemini was used
}
