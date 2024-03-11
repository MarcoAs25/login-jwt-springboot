package com.marco.loginjwt.domain.email;

import com.marco.loginjwt.domain.auth.confirmation_code.ConfirmationCode;
import com.marco.loginjwt.domain.auth.confirmation_code.ConfirmationCodeService;
import com.marco.loginjwt.domain.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final ConfirmationCodeService confirmationCodeService;
    private final TemplateEngine templateEngine;
    public void sendMail(String to, String subject, String body) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        String htmlMsg = body;
        try {
            helper.setText(htmlMsg, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("noreply@gmail.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(message);

    }
    @Async
    public void sendMailConfirmation(User user) {
        ConfirmationCode confirmationCode = confirmationCodeService.createConfirmationCode(user);
        String code = confirmationCode.getCode();
        String systemName = "login-jwt";
        String title = String.format("Bem vindo ao sistema %s %s!", systemName, user.getName());

        Context context = new Context();
        context.setVariable("title",title);
        context.setVariable("code",code);
        context.setVariable("systemName",systemName);

        String body = templateEngine.process("email/confirmacao-conta", context);
        context.clearVariables();
        templateEngine.clearTemplateCache();

        sendMail(user.getEmail(), "Código de confirmação de conta", body);
    }
}
