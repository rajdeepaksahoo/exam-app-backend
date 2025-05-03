package com.online.exam.service;

public interface ReportService {
    public byte[] generateReport(Long examId, String name, String examOn, Long totalQuestions,
                                 Long attendedQuestions, Long mark,Long correctAnswers) throws Exception ;
}
