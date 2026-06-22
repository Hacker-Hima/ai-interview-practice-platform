package com.himachalam.aiinterview.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Binds the interview submission form: session ID + per-question answers.
 * HTML form fields: sessionId, answers[questionId]=text
 */
public class InterviewSubmitDto {

    private Long sessionId;
    private Map<String, String> answers = new HashMap<>();

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Map<String, String> getAnswers() { return answers; }
    public void setAnswers(Map<String, String> answers) { this.answers = answers; }
}
