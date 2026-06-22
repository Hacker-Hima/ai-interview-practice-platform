package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.model.Question;
import com.himachalam.aiinterview.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public List<Question> getByDomain(Long domainId) {
        return questionRepository.findByDomain_Id(domainId);
    }

    @Override
    public List<Question> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return getAllQuestions();
        return questionRepository
                .findByQuestionTextContainingIgnoreCaseOrExpectedAnswerContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<Question> getByDomainAndDifficulty(Long domainId, String difficulty) {
        if (domainId != null && difficulty != null && !difficulty.isBlank()) {
            return questionRepository.findByDomain_IdAndDifficulty(domainId, difficulty);
        } else if (domainId != null) {
            return questionRepository.findByDomain_Id(domainId);
        } else if (difficulty != null && !difficulty.isBlank()) {
            return questionRepository.findByDifficulty(difficulty);
        }
        return getAllQuestions();
    }

    @Override
    public Question getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found: " + id));
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public long countByDomain(Long domainId) {
        return questionRepository.countByDomain_Id(domainId);
    }
}
