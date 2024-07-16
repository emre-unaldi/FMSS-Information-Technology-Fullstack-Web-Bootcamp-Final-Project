package unaldi.orderservice.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import unaldi.orderservice.entity.dto.EmailDetailsDTO;
import unaldi.orderservice.service.EmailService;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(EmailDetailsDTO emailDetailsDTO) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(emailDetailsDTO.getRecipient());
        mailMessage.setText(emailDetailsDTO.getBody());
        mailMessage.setSubject(emailDetailsDTO.getSubject());

        javaMailSender.send(mailMessage);

        log.info("Order invoice successfully sent to email : {}", emailDetailsDTO.getRecipient());
    }

}
