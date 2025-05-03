package com.online.exam.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private Integer port;
    @Bean
    public JavaMailSender javaMailSender(){
        var mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setUsername(username);
        mailSenderImpl.setPassword(password);
        mailSenderImpl.setHost(host);
        mailSenderImpl.setPort(port);

        Properties props = mailSenderImpl.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSenderImpl;
    }
}
