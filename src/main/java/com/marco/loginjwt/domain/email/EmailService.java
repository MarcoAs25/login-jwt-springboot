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

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final ConfirmationCodeService confirmationCodeService;

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

        String body = "<html><body>";
        body += "<p>Olá " + user.getName() + ",</p>";
        body += "<p>Seu código de confirmação de conta é: <strong>" + confirmationCode.getCode() + "</strong></p>";
        body += "<p>Caso não tenha criado uma conta em login-jwt, por favor, desconsidere esta mensagem.</p>";
        body += "</body></html>";

        sendMail(user.getEmail(), "Código de confirmação de conta", body);
    }
}
