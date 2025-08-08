package units;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Utility: send plain-text UTF-8 email.
 */
public class SendMail {
    public static void sendMail(String toEmail, String subject, String messageText) throws MessagingException, UnsupportedEncodingException {
        final String fromEmail = EmailConfig.getEmail();
        final String password = EmailConfig.getPassword();

        if (fromEmail == null || fromEmail.isBlank() || password == null || password.isBlank()) {
            throw new IllegalStateException("Email credentials not set. Please set system properties 'email' and 'password'.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.mime.charset", "UTF-8");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail, "COCOLAND"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(messageText);  // <--- Sửa đúng biến này
        Transport.send(msg);
    }
}