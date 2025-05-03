package com.online.exam.repository;

import com.online.exam.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
}
