package com.himachalam.aiinterview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.himachalam.aiinterview.dto.EvaluationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Evaluates student answers using:
 *  1. Google Gemini API (primary)
 *  2. Keyword matching (fallback when API is unavailable or key not set)
 */
@Service
public class EvaluationService {

    private static final Logger log = LoggerFactory.getLogger(EvaluationService.class);

    private static final Set<String> STOP_WORDS = Set.of(
            "the","a","an","is","are","was","were","be","been","being","have","has","had",
            "do","does","did","will","would","could","should","may","might","must","can",
            "to","of","in","on","at","by","for","with","about","as","into","through",
            "during","it","its","this","that","these","those","i","we","you","he","she",
            "they","and","or","but","if","because","which","when","where","how","what",
            "then","than","so","from","up","out","also","not","no","more","just","each"
    );

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public EvaluationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Public entry point
    // ─────────────────────────────────────────────────────────────────────────

    public EvaluationResult evaluate(String questionText, String expectedAnswer, String userAnswer) {
        boolean geminiConfigured = geminiApiKey != null
                && !geminiApiKey.isBlank()
                && !geminiApiKey.equals("YOUR_GEMINI_API_KEY");

        if (geminiConfigured) {
            try {
                return callGeminiAPI(questionText, expectedAnswer, userAnswer);
            } catch (Exception ex) {
                log.warn("Gemini API evaluation failed ({}), using keyword fallback.", ex.getMessage());
            }
        }
        return keywordMatch(expectedAnswer, userAnswer);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Gemini API evaluation
    // ─────────────────────────────────────────────────────────────────────────

    private EvaluationResult callGeminiAPI(String questionText, String expectedAnswer, String userAnswer) throws Exception {
        String prompt = buildPrompt(questionText, expectedAnswer, userAnswer);

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String url = geminiApiUrl + "?key=" + geminiApiKey;
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        JsonNode root = objectMapper.readTree(response.getBody());
        String rawText = root
                .path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text").asText();

        // Strip markdown code fences if present
        rawText = rawText.replaceAll("(?s)```json\\s*", "").replaceAll("```", "").trim();

        // Extract the JSON object even if wrapped in prose
        int start = rawText.indexOf('{');
        int end = rawText.lastIndexOf('}');
        if (start >= 0 && end > start) {
            rawText = rawText.substring(start, end + 1);
        }

        JsonNode result = objectMapper.readTree(rawText);

        return EvaluationResult.builder()
                .score(Math.min(100, Math.max(0, result.path("score").asInt(50))))
                .feedback(result.path("feedback").asText("Evaluated by AI."))
                .strengths(parseStringList(result.path("strengths")))
                .weaknesses(parseStringList(result.path("weaknesses")))
                .recommendedTopics(parseStringList(result.path("recommendedTopics")))
                .aiEvaluated(true)
                .build();
    }

    private String buildPrompt(String question, String expected, String userAnswer) {
        return """
                You are an expert technical interview evaluator. Evaluate this interview answer strictly and fairly.

                QUESTION: %s

                REFERENCE ANSWER: %s

                STUDENT'S ANSWER: %s

                Evaluate based on: technical accuracy, coverage of key concepts, and clarity.
                If the student's answer is empty or very short, give a score of 0-10.

                Return ONLY a valid JSON object (no markdown, no extra text):
                {
                  "score": <integer 0-100>,
                  "feedback": "<2-3 sentence overall feedback>",
                  "strengths": ["<strength 1>", "<strength 2>"],
                  "weaknesses": ["<weakness 1>", "<weakness 2>"],
                  "recommendedTopics": ["<topic 1>", "<topic 2>"]
                }
                """.formatted(question, expected, userAnswer);
    }

    private List<String> parseStringList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node != null && node.isArray()) {
            for (JsonNode item : node) {
                String text = item.asText("").trim();
                if (!text.isBlank()) list.add(text);
            }
        }
        return list;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Keyword matching fallback
    // ─────────────────────────────────────────────────────────────────────────

    private EvaluationResult keywordMatch(String expectedAnswer, String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            return EvaluationResult.builder()
                    .score(0)
                    .feedback("No answer was provided.")
                    .strengths(List.of())
                    .weaknesses(List.of("No answer provided"))
                    .recommendedTopics(List.of())
                    .aiEvaluated(false)
                    .build();
        }

        Set<String> expectedKeywords = extractKeywords(expectedAnswer);
        Set<String> userKeywords = extractKeywords(userAnswer);

        long matched = expectedKeywords.stream().filter(userKeywords::contains).count();
        int score = expectedKeywords.isEmpty() ? 50
                : (int) Math.min(100, (matched * 100.0 / expectedKeywords.size()));

        List<String> strengths = new ArrayList<>();
        List<String> weaknesses = new ArrayList<>();
        List<String> recommended = new ArrayList<>();

        if (score >= 80) strengths.add("Good coverage of key concepts");
        if (score >= 60) strengths.add("Relevant terminology used");
        if (score < 60) weaknesses.add("Missing several key concepts");
        if (score < 40) {
            weaknesses.add("Answer lacks sufficient detail");
            recommended.add("Review the topic thoroughly");
        }

        String feedback = String.format(
                "Keyword analysis: %d of %d key concepts matched (%.0f%%). %s",
                matched, expectedKeywords.size(), (double) score,
                score >= 70 ? "Good understanding demonstrated." : "More detail needed.");

        return EvaluationResult.builder()
                .score(score)
                .feedback(feedback)
                .strengths(strengths)
                .weaknesses(weaknesses)
                .recommendedTopics(recommended)
                .aiEvaluated(false)
                .build();
    }

    private Set<String> extractKeywords(String text) {
        if (text == null) return Collections.emptySet();
        return Arrays.stream(text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").split("\\s+"))
                .filter(w -> w.length() > 2 && !STOP_WORDS.contains(w))
                .collect(Collectors.toSet());
    }
}
