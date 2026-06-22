package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.model.Answer;
import com.himachalam.aiinterview.model.InterviewSession;
import com.himachalam.aiinterview.model.User;

import java.util.List;
import java.util.Map;

public interface InterviewService {

    /** Create a new session and pick 10 random questions from the domain. */
    InterviewSession startInterview(String userEmail, Long domainId);

    /** Evaluate submitted answers, persist results, mark session COMPLETED. */
    InterviewSession submitInterview(Long sessionId, Map<String, String> answers);

    InterviewSession getSessionById(Long id);

    List<InterviewSession> getSessionsByUser(User user);

    List<Answer> getAnswersForSession(Long sessionId);
}
