package com.online.exam.service;

import com.online.exam.dto.QuestionAndAnswerBulkUploadResponse;
import com.online.exam.model.UploadedFile;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionAndAnswerBulkUploadFileService {
    public QuestionAndAnswerBulkUploadResponse uploadBulkQuestionAndAnswer(MultipartFile multipartFile,boolean saveData,QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest);
    public UploadedFile getUploadedFile(QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest);
    public byte[] downloadBlankExcel();
}
