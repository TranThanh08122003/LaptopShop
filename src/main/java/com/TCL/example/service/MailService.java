package com.TCL.example.service;

import com.TCL.example.domain.DTO.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.getTo());
        message.setFrom("tranthanh2003tdt@gmail.com");
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getContent());
        javaMailSender.send(message);
    }

}