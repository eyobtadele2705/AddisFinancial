package com.programming.keycloak.event.listener;

import com.programming.keycloak.event.RegistrationCompleteEvent;
import com.programming.keycloak.model.User;
import com.programming.keycloak.service.UserService;
import com.programming.keycloak.service.impl.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserServiceImpl userService;

    private final JavaMailSender mailSender;
    private User theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        theUser = event.getUser();

        String verificationToken = UUID.randomUUID().toString();

        userService.saveVerificationToken(theUser, verificationToken);

        String redirectUrl = event.getApplicationUrl()+ "/users/verifyEmail?token="+verificationToken;
//        String redirectUrl = "http://localhost:4200/complete-registration";
//        String redirectUrl = "http://204.10.160.9/complete-registration";

        try {
            sendVerificationEmail(redirectUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("URL is - {}", redirectUrl);
    }

    public void sendVerificationEmail(String redirectUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Lottery App Registration Service";
        String mailContent = "<p> Hi, "+ theUser.fullName()+ ", </p>"+
                "<p>Thank you for registering with us,</p>"+
                "<p>Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +redirectUrl+ "\">Complete Registration.</a>"+
                "<p>The above link will expire in 15 minutes.</p>"+
                "<p> Thank you <br> Lottery App Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("eyob1810@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
