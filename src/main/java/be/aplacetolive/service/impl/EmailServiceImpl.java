package be.aplacetolive.service.impl;

import be.aplacetolive.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by medard on 03.07.17.
 */

@Component
public class EmailServiceImpl implements EmailService {

    private static Logger logger = LoggerFactory.getLogger("Email Logger");

    @Autowired
    public JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMessage(String[] to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Aplace2Live Admin <admin@aplace2live.tk>");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
        } catch (MailException ex){
            logger.error(ex.getMessage());
        }

    }
}
