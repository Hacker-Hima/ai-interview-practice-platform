package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.dto.AdminStatsDto;
import com.himachalam.aiinterview.model.User;
import com.himachalam.aiinterview.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final DomainRepository domainRepository;
    private final InterviewSessionRepository sessionRepository;

    public AdminServiceImpl(UserRepository userRepository,
                            QuestionRepository questionRepository,
                            DomainRepository domainRepository,
                            InterviewSessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.domainRepository = domainRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public AdminStatsDto getStats() {
        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalQuestions(questionRepository.count());
        stats.setTotalDomains(domainRepository.count());
        stats.setTotalInterviews(sessionRepository.countAllCompleted());
        Double avg = sessionRepository.findOverallAvgScore();
        stats.setOverallAvgScore(avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0);
        return stats;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
