package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import model.Order;
import model.OrderDetail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import units.SendMail;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NotificationService {
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    // Thay đổi tên biến môi trường ở đây
    private static final String ZALO_ACCESS_TOKEN = System.getenv("ZALO_ACCESS_TOKEN");
    private static final String ZALO_TEMPLATE_ID = System.getenv("ZALO_TEMPLATE_ID");
    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy");

    public void sendOrderEmail(String toEmail, Order order, List<OrderDetail> details) throws MessagingException, UnsupportedEncodingException {
        if (toEmail == null || toEmail.isBlank()) {
            LOGGER.warning("Recipient email is empty; skip sending email");
            return;
        }
        String subject = "Thông tin đơn hàng #" + order.getId();
        StringBuilder body = new StringBuilder();
        body.append("Chào quý khách,\n\n")
            .append("Sản phẩm đã đặt: ").append(formatItems(details)).append("\n")
            .append("Tổng tiền: ").append(order.getTotal()).append("\n")
            .append("Tiền đã thanh toán: ").append(order.getDeposit()).append("\n")
            .append("Ngày đặt: ").append(DF.format(order.getOrderDate())).append("\n")
            .append("Ngày hẹn: ").append(DF.format(order.getDeliveryDate())).append("\n\n")
            .append("Cảm ơn bạn!");
        SendMail.sendMail(toEmail, subject, body.toString());
        LOGGER.info("Sent order email to " + toEmail);
    }

    public void sendOrderCompletionEmail(String toEmail, Order order, List<OrderDetail> details) throws MessagingException, UnsupportedEncodingException {
        if (toEmail == null || toEmail.isBlank()) {
            LOGGER.warning("Recipient email is empty; skip sending email");
            return;
        }
        String subject = "Đơn hàng #" + order.getId() + " đã hoàn thành";
        StringBuilder body = new StringBuilder();
        body.append("Chào quý khách,\n\n")
            .append("Đơn hàng của bạn đã hoàn thành.\n")
            .append("Sản phẩm đã đặt: ").append(formatItems(details)).append("\n")
            .append("Tổng tiền: ").append(order.getTotal()).append("\n")
            .append("Tiền đã thanh toán: ").append(order.getDeposit()).append("\n")
            .append("Ngày đặt: ").append(DF.format(order.getOrderDate())).append("\n")
            .append("Ngày hẹn: ").append(DF.format(order.getDeliveryDate())).append("\n\n")
            .append("Cảm ơn bạn!");
        SendMail.sendMail(toEmail, subject, body.toString());
        LOGGER.info("Sent order completion email to " + toEmail);
    }

    public void sendOrderZns(String phone, Order order, List<OrderDetail> details) {
        if (ZALO_ACCESS_TOKEN == null || ZALO_TEMPLATE_ID == null) {
            LOGGER.warning("Zalo credentials not set; skip sending ZNS");
            return;
        }
        String phoneZns = normalizePhone(phone);
        if (phoneZns == null || phoneZns.isBlank()) {
            LOGGER.warning("Recipient phone is empty; skip sending ZNS");
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
            payload.put("phone", phoneZns);
            payload.put("template_id", ZALO_TEMPLATE_ID);
            payload.put("template_data", templateData);
            payload.put("tracking_id", "order_" + order.getId());

            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(payload);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            int code = conn.getResponseCode();
            StringBuilder resp = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    code >= 400 ? conn.getErrorStream() : conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resp.append(line);
                }
            }
            LOGGER.info("ZNS response: HTTP " + code + " - " + resp);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Send ZNS failed", ex);
        }
    }

    private String formatItems(List<OrderDetail> details) {
        return details.stream()
                .map(d -> d.getProductType() + " x" + d.getQuantity())
                .collect(Collectors.joining(", "));
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        String digits = phone.replaceAll("\\D", "");
        if (digits.isEmpty()) return null;
        if (digits.startsWith("0")) {
            digits = "84" + digits.substring(1);
        } else if (!digits.startsWith("84")) {
            digits = "84" + digits;
        }
        return digits;
    }
}
