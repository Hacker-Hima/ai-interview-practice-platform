package com.himachalam.aiinterview.repository;

import com.himachalam.aiinterview.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByDomain_Id(Long domainId);

    List<Question> findByDifficulty(String difficulty);

    List<Question> findByDomain_IdAndDifficulty(Long domainId, String difficulty);

    List<Question> findByQuestionTextContainingIgnoreCaseOrExpectedAnswerContainingIgnoreCase(
            String keyword1, String keyword2);

    @Query(value = "SELECT * FROM question WHERE domain_id = :domainId ORDER BY RAND() LIMIT :lim",
           nativeQuery = true)
    List<Question> findRandomByDomain(@Param("domainId") Long domainId, @Param("lim") int limit);

    long countByDomain_Id(Long domainId);
}