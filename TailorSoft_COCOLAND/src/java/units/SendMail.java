package units;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Hàm tiện ích gửi email (text/plain UTF‑8).
 */
public class SendMail {
    public static void sendMail(String toEmail, String subject, String messageText) throws MessagingException, UnsupportedEncodingException {
        final String fromEmail = EmailConfig.getEmail();
        final String password = EmailConfig.getPassword();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.mime.charset", "UTF-8");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pwd);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from, "COCOLAND"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subject);
        msg.setText(text);
        Transport.send(msg);
    }
}