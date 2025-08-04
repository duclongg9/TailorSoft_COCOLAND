package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import model.Order;
import model.OrderDetail;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class NotificationService {
    private static final String GMAIL_USER = System.getenv("GMAIL_USER");
    private static final String GMAIL_PASS = System.getenv("GMAIL_PASS");
    private static final String ZALO_ACCESS_TOKEN = System.getenv("ZALO_ACCESS_TOKEN");
    private static final String ZALO_TEMPLATE_ID = System.getenv("ZALO_TEMPLATE_ID");
    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy");

    public void sendOrderEmail(String toEmail, Order order, List<OrderDetail> details) throws MessagingException {
        if (GMAIL_USER == null || GMAIL_PASS == null || toEmail == null || toEmail.isBlank()) {
            return;
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USER, GMAIL_PASS);
            }
        });

        String subject = "Thông tin đơn hàng #" + order.getId();
        StringBuilder body = new StringBuilder();
        body.append("Chào quý khách,\n\n")
            .append("Sản phẩm đã đặt: ").append(formatItems(details)).append("\n")
            .append("Tổng tiền: ").append(order.getTotal()).append("\n")
            .append("Tiền đã thanh toán: ").append(order.getDeposit()).append("\n")
            .append("Ngày đặt: ").append(DF.format(order.getOrderDate())).append("\n")
            .append("Ngày hẹn: ").append(DF.format(order.getDeliveryDate())).append("\n\n")
            .append("Cảm ơn bạn!");

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(GMAIL_USER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body.toString());

        Transport.send(message);
    }

    public void sendOrderZns(String phone, Order order, List<OrderDetail> details) {
        if (ZALO_ACCESS_TOKEN == null || ZALO_TEMPLATE_ID == null || phone == null || phone.isBlank()) {
            return;
        }
        try {
            URL url = new URL("https://business.openapi.zalo.me/message/template");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("access_token", ZALO_ACCESS_TOKEN);
            conn.setDoOutput(true);

            Map<String, Object> templateData = new HashMap<>();
            templateData.put("items", formatItems(details));
            templateData.put("total", String.valueOf(order.getTotal()));
            templateData.put("paid", String.valueOf(order.getDeposit()));
            templateData.put("order_date", DF.format(order.getOrderDate()));
            templateData.put("appointment_date", DF.format(order.getDeliveryDate()));

            Map<String, Object> payload = new HashMap<>();
            payload.put("phone", phone);
            payload.put("template_id", ZALO_TEMPLATE_ID);
            payload.put("template_data", templateData);
            payload.put("tracking_id", "order_" + order.getId());

            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(payload);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            conn.getResponseCode(); // trigger request
            conn.disconnect();
        } catch (Exception ignored) {
        }
    }

    private String formatItems(List<OrderDetail> details) {
        return details.stream()
                .map(d -> d.getProductType() + " x" + d.getQuantity())
                .collect(Collectors.joining(", "));
    }
}

