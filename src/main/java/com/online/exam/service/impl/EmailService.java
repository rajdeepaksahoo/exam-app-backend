package com.online.exam.service.impl;

import com.online.exam.exception.QAppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfData, String fileName) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true indicates multipart message

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // Attach the PDF file using ByteArrayResource
        InputStreamSource attachment = new ByteArrayResource(pdfData);
//        helper.addAttachment(fileName, attachment);
        helper.addAttachment("examReport.pdf", new ByteArrayResource(pdfData)); // Adding the PDF as an attachment
        javaMailSender.send(mimeMessage);
    }

    public void sendOtp(String to, String subject, String text)  {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true indicates multipart message

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            throw new QAppException(e.getMessage());
        }
    }
}

