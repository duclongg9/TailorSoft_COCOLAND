package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import model.Order;
import model.OrderDetail;
import model.Customer;

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
import units.EmailConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NotificationService {
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    // Thay đổi tên biến môi trường ở đây
    private static final String ZALO_ACCESS_TOKEN = System.getenv("ZALO_ACCESS_TOKEN");
    private static final String ZALO_TEMPLATE_ID = System.getenv("ZALO_TEMPLATE_ID");
    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy");

    public boolean sendOrderEmail(Customer customer, Order order, List<OrderDetail> details) {
        if (customer == null || customer.getEmail() == null || customer.getEmail().isBlank()) {
            LOGGER.warning("Recipient email is empty; skip sending email");
            return false;
        }
        if (!EmailConfig.isConfigured()) {
            LOGGER.warning("Email credentials not configured; skip sending email");
            return false;
        }
        String toEmail = customer.getEmail();
        String subject = "[COCOLAND] Xác nhận đơn hàng #" + order.getId() + " – Cảm ơn " + customer.getName();
        StringBuilder body = new StringBuilder();
        body.append("Kính chào ").append(customer.getName()).append(",\n\n")
            .append("Cảm ơn Quý khách đã tin chọn COCOLAND. Chúng tôi xin gửi tới Quý khách thông tin đơn hàng vừa đặt ngày ")
            .append(DF.format(order.getOrderDate())).append(" như sau:\n\n")
            .append("Thông tin đơn hàng:\n\n")
            .append("• Mã đơn hàng: ").append(order.getId()).append("\n")
            .append("• Sản phẩm:\n");
        for (OrderDetail d : details) {
            body.append("  • ").append(d.getProductType()).append(" x").append(d.getQuantity()).append("\n");
        }
        body.append("• Tổng tiền: ").append(order.getTotal()).append(" VND\n")
            .append("- Tiền cọc đã thanh toán: ").append(order.getDeposit()).append(" VND\n")
            .append("• Địa chỉ giao hàng: ")
            .append(customer.getAddress() != null ? customer.getAddress() : "").append("\n\n")
            .append("Trạng thái đơn hàng: ").append(order.getStatus()).append("\n")
            .append("Ngày giao dự kiến: ").append(DF.format(order.getDeliveryDate())).append("\n\n")
            .append("Nếu cần hỗ trợ, xin vui lòng liên hệ:\n")
            .append("• Điện thoại: 03.8888888.65\n")
            .append("• Email: support@cocoland.vn\n")
            .append("• Zalo/WeChat: @cocoland_support\n\n")
            .append("Chúng tôi luôn nỗ lực mang đến sản phẩm may đo chuẩn, phù hợp với nhu cầu riêng của từng khách hàng.\n")
            .append("Cảm ơn Quý khách đã đồng hành cùng COCOLAND!\n\n")
            .append("Trân trọng,\n")
            .append("Nguyễn Hoàng Thái Thịnh\n")
            .append("Bộ phận Chăm sóc Khách hàng – COCOLAND");
        try {
            SendMail.sendMail(toEmail, subject, body.toString());
            LOGGER.info("Sent order email to " + toEmail);
            return true;
        } catch (MessagingException | UnsupportedEncodingException ex) {
            LOGGER.log(Level.WARNING, "Send order email failed", ex);
            return false;
        } catch (IllegalStateException ex) {
            LOGGER.warning("Send order email failed: " + ex.getMessage());
            return false;
        }
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
