package units;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.UnsupportedEncodingException;

public class SendMail {
    public static void sendMail(String toEmail, String subject, String messageText) throws MessagingException, UnsupportedEncodingException {
        if (!EmailConfig.isConfigured()) {
            throw new IllegalStateException("Email credentials not configured");
        }
        final String fromEmail = EmailConfig.getEmail();
        final String password = EmailConfig.getPassword();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail, "TailorSoft"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        msg.setSubject(subject);
        msg.setText(messageText);

        Transport.send(msg);
    }
}
