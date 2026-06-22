package com.himachalam.aiinterview.repository;

import com.himachalam.aiinterview.model.Answer;
import com.himachalam.aiinterview.model.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findBySessionOrderByQuestion_Id(InterviewSession session);

    List<Answer> findBySession(InterviewSession session);

    long countBySession(InterviewSession session);
}
