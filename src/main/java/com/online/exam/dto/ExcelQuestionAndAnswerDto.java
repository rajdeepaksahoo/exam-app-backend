package com.online.exam.dto;

import com.online.exam.model.Answer;
import com.online.exam.model.Questions;
import lombok.Data;

import java.util.List;
@Data
public class ExcelQuestionAndAnswerDto {
    private List<Questions> questionsList;
    private QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadResponse;
}
