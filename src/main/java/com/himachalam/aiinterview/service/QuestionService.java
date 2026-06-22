package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.model.Question;
import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions();
    List<Question> getByDomain(Long domainId);
    List<Question> search(String keyword);
    List<Question> getByDomainAndDifficulty(Long domainId, String difficulty);
    Question getById(Long id);
    Question save(Question question);
    void delete(Long id);
    long countByDomain(Long domainId);
}
