package com.online.exam.dto;

import com.online.exam.model.Questions;
import lombok.Data;

import java.util.List;

@Data
public class GetResultDto {
    private List<String> givenAnswers;
    private List<Questions> questionSet;
    private Long examId;
}
