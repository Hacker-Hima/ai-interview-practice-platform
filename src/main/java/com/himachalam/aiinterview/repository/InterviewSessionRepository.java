package com.himachalam.aiinterview.repository;

import com.himachalam.aiinterview.model.InterviewSession;
import com.himachalam.aiinterview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

    List<InterviewSession> findByUserOrderByStartTimeDesc(User user);

    List<InterviewSession> findByUserAndStatusOrderByStartTimeDesc(User user, String status);

    long countByUser(User user);

    long countByUserAndStatus(User user, String status);

    @Query("SELECT AVG(s.score) FROM InterviewSession s WHERE s.user = :user AND s.score IS NOT NULL AND s.status = 'COMPLETED'")
    Double findAvgScoreByUser(@Param("user") User user);

    @Query("SELECT MAX(s.score) FROM InterviewSession s WHERE s.user = :user AND s.score IS NOT NULL AND s.status = 'COMPLETED'")
    Double findMaxScoreByUser(@Param("user") User user);

    Optional<InterviewSession> findTopByUserAndStatusOrderByStartTimeDesc(User user, String status);

    @Query("SELECT COUNT(s) FROM InterviewSession s WHERE s.status = 'COMPLETED'")
    long countAllCompleted();

    @Query("SELECT AVG(s.score) FROM InterviewSession s WHERE s.score IS NOT NULL AND s.status = 'COMPLETED'")
    Double findOverallAvgScore();
}
