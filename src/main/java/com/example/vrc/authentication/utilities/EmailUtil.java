package com.example.vrc.authentication.utilities;


import io.jsonwebtoken.Jwt;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private JwtUtil jwtUtil;

    public void sendSetEmailPassword(String email) throws MessagingException {
        String token = jwtUtil.generateToken(email);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        String resetLink = "https://vrc-pe8g.onrender.com/reset-password/" + token;

        String emailContent = String.format("<p>Click the link to set your password: <a href='%s'>%s</a></p>", resetLink, resetLink);

        mimeMessageHelper.setText(emailContent, true);

        javaMailSender.send(mimeMessage);
    }
}
