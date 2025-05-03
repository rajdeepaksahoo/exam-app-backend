package com.online.exam.repository;

import com.online.exam.model.SubmittedAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubmittedAnswerRepository extends JpaRepository<SubmittedAnswer,Long> {
    @Query("SELECT COALESCE(MAX(sa.examId), 0)+1 as examId FROM SubmittedAnswer sa")
    public Long getExamId();
}
