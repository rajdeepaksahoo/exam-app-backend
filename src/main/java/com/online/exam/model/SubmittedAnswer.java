package com.online.exam.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class SubmittedAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SUBMITTED_ANSWER_ID")
    private Long id;
    private Long submittedQuestionId;
    private String submittedAnswer;
    private Long examId;
    private Long userId;
}
