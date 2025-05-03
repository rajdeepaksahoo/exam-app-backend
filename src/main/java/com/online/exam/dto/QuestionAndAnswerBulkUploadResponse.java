package com.online.exam.dto;

import lombok.Data;

@Data
public class QuestionAndAnswerBulkUploadResponse {
    private Long numberOfValidEntries;
    private Long numberOfInvalidEntries;
    private String originalFileUrl;
    private String errorFileUrl;
    private String errorReason;
    private String uploadedFileName;
}
