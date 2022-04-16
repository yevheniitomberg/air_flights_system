package tomberg.fun.spring.air_flights.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tomberg.fun.spring.air_flights.entity.User;

import java.util.Random;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public String confirmLinkGenerating() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public boolean registrationConfirmEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        String generatedLink = confirmLinkGenerating();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Registration confirm");
        message.setText("Click here to confirm registration: http://localhost:8080/register/confirm/" + generatedLink);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            return true;
        }
        user.setConfirmLink(generatedLink);
        user.setFullyRegistered(false);
        return false;
    }
}
