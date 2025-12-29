package com.example.project_interview.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Autowired
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message , true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to {} with subject {}", to, subject, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}", to, e);
            throw new MessagingException("Failed to send email");
        }
    }
}
