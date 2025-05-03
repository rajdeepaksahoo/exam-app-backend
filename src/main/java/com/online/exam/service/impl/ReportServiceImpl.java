package com.online.exam.service.impl;

import com.online.exam.service.ReportService;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private DataSource dataSource; // Inject your DataSource bean

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public byte[] generateReport(Long examId, String name, String examOn, Long totalQuestions,
                                 Long attendedQuestions, Long mark,Long correctAnswers) throws Exception {
        // Load the .jrxml file from the classpath
        InputStream reportStream = new ClassPathResource("reports/examSummery.jrxml").getInputStream();

        // Compile the Jasper report from .jrxml to .jasper
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Create parameters map to pass to the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("examId", examId);
        parameters.put("name", name);
        parameters.put("examOn", examOn);
        parameters.put("totalQuestions", totalQuestions);
        parameters.put("attendedQuestions", attendedQuestions);
        parameters.put("mark", mark);
        parameters.put("correctAnswers", mark);
        byte[] bytes=null;
        // Create a JDBC connection
        try (Connection connection = dataSource.getConnection()) {
            // Generate the JasperPrint object (populated report) using the database connection
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Export the report to PDF (or any other format)
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        }
        // Export the report to PDF (or any other format)
        return bytes;
    }
}
