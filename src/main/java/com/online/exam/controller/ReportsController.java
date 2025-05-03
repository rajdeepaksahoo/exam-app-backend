package com.online.exam.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ReportsController {
    @PostMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody Map<String,String> base64Data) {
        // Simulating the byte array (you would get this from the database or elsewhere)
        byte[] pdfByteArray = java.util.Base64.getDecoder().decode(base64Data.get("reportData"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "examReport.pdf");
        return new ResponseEntity<>(pdfByteArray, headers, HttpStatus.OK);
    }
}
