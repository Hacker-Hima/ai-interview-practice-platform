package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.dto.EvaluationResult;
import com.himachalam.aiinterview.model.*;
import com.himachalam.aiinterview.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewSessionRepository sessionRepo;
    private final AnswerRepository answerRepo;
    private final QuestionRepository questionRepo;
    private final DomainRepository domainRepo;
    private final UserRepository userRepo;
    private final EvaluationService evaluationService;

    public InterviewServiceImpl(InterviewSessionRepository sessionRepo,
                                AnswerRepository answerRepo,
                                QuestionRepository questionRepo,
                                DomainRepository domainRepo,
                                UserRepository userRepo,
                                EvaluationService evaluationService) {
        this.sessionRepo = sessionRepo;
        this.answerRepo = answerRepo;
        this.questionRepo = questionRepo;
        this.domainRepo = domainRepo;
        this.userRepo = userRepo;
        this.evaluationService = evaluationService;
    }

    @Override
    public InterviewSession startInterview(String userEmail, Long domainId) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Domain domain = domainRepo.findById(domainId)
                .orElseThrow(() -> new RuntimeException("Domain not found: " + domainId));

        List<Question> questions = questionRepo.findRandomByDomain(domainId, 10);
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions available for this domain yet.");
        }

        InterviewSession session = new InterviewSession();
        session.setUser(user);
        session.setDomain(domain);
        session.setStartTime(LocalDateTime.now());
        session.setStatus("IN_PROGRESS");
        session.setQuestions(questions);

        return sessionRepo.save(session);
    }

    @Override
    public InterviewSession submitInterview(Long sessionId, Map<String, String> answers) {
        InterviewSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));

        // Guard: already completed?
        if ("COMPLETED".equals(session.getStatus())) {
            return session;
        }

        List<Question> questions = session.getQuestions(); // triggers lazy load within transaction
        List<Answer> savedAnswers = new ArrayList<>();
        int totalScore = 0;

        for (Question question : questions) {
            String userAnswer = answers.getOrDefault(String.valueOf(question.getId()), "").trim();

            EvaluationResult result = evaluationService.evaluate(
                    question.getQuestionText(),
                    question.getExpectedAnswer(),
                    userAnswer
            );

            Answer answer = new Answer();
            answer.setSession(session);
            answer.setQuestion(question);
            answer.setUserAnswer(userAnswer);
            answer.setScore(result.getScore());
            answer.setAiFeedback(result.getFeedback());
            answer.setStrengths(result.getStrengths() != null
                    ? String.join(";", result.getStrengths()) : "");
            answer.setWeaknesses(result.getWeaknesses() != null
                    ? String.join(";", result.getWeaknesses()) : "");
            answer.setRecommendedTopics(result.getRecommendedTopics() != null
                    ? String.join(";", result.getRecommendedTopics()) : "");
            answer.setEvaluatedByAI(result.isAiEvaluated());

            savedAnswers.add(answerRepo.save(answer));
            totalScore += result.getScore();
        }

        double avgScore = questions.isEmpty() ? 0.0 : (double) totalScore / questions.size();

        session.setScore(Math.round(avgScore * 10.0) / 10.0); // round to 1 decimal
        session.setEndTime(LocalDateTime.now());
        session.setStatus("COMPLETED");

        return sessionRepo.save(session);
    }

    @Override
    public InterviewSession getSessionById(Long id) {
        return sessionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found: " + id));
    }

    @Override
    public List<InterviewSession> getSessionsByUser(User user) {
        return sessionRepo.findByUserOrderByStartTimeDesc(user);
    }

    @Override
    public List<Answer> getAnswersForSession(Long sessionId) {
        InterviewSession session = getSessionById(sessionId);
        return answerRepo.findBySessionOrderByQuestion_Id(session);
    }
}
