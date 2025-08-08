package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.mail.MessagingException;
import model.Customer;
import model.Order;
import model.OrderDetail;
import units.EmailConfig;
import units.SendMail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Gửi email và ZNS cho đơn hàng.
 */
public class NotificationService {
    private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());
    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final String ZALO_ACCESS_TOKEN = System.getenv("ZALO_ACCESS_TOKEN");
    private static final String ZALO_TEMPLATE_ID  = System.getenv("ZALO_TEMPLATE_ID");

    // Public facade
    public void notifyOrder(Customer customer, Order order, List<OrderDetail> details) {
        boolean ok = sendOrderEmail(customer, order, details);
        if (!ok) LOG.warning("Order email not sent");
        sendOrderZns(customer != null ? customer.getPhone() : null, order, details);
    }

    // -------- EMAIL -------- //
    public boolean sendOrderEmail(Customer customer, Order order, List<OrderDetail> details) {
        if (customer == null || customer.getEmail() == null || customer.getEmail().isBlank()) {
            return false;

        }
        if (!EmailConfig.isConfigured()) {
            LOG.warning("Email credentials not configured; skip");
            return false;
        }

        String to = customer.getEmail();
        String subject = buildEmailSubject(customer, order);
        String body = buildEmailBody(customer, order, details);
        try {
            SendMail.sendMail(to, subject, body);
            LOG.info("Sent order email to " + to);
            return true;
        } catch (MessagingException | IOException ex) {
            LOG.log(Level.WARNING, "Send order email failed", ex);
            return false;
        }
    }

    private String buildEmailBody(Customer c, Order o, List<OrderDetail> dts) {
        String items = dts.stream()
                .map(d -> "• " + d.getProductType() + " × " + d.getQuantity())
                .collect(Collectors.joining("\n"));
        double remaining = o.getTotal() - o.getDeposit();
        return new StringBuilder()
                .append("Kính gửi anh/chị ").append(c.getName()).append(",\n\n")
                .append("Cảm ơn Quý khách đã tin tưởng và đặt hàng tại COCOLAND.\n\n")
                .append("Thông tin đơn hàng của Quý khách:\n\n")
                .append("Mã đơn hàng: #").append(o.getId()).append("\n")
                .append("Sản phẩm:\n").append(items).append("\n")
                .append("Ngày đặt hàng: ").append(DF.format(o.getOrderDate())).append("\n")
                .append("Ngày giao dự kiến: ").append(DF.format(o.getDeliveryDate())).append("\n")
                .append("Tổng giá trị đơn hàng: ").append(o.getTotal()).append(" VND\n")
                .append("Tổng tiền cọc đã thanh toán: ").append(o.getDeposit()).append(" VND\n")
                .append("Số tiền còn lại: ").append(remaining).append(" VND\n")
                .append("Trạng thái đơn hàng: ").append(friendlyStatus(o.getStatus())).append("\n")
                .append("Địa chỉ giao hàng: ").append(c.getAddress()).append("\n\n")
                .append("Chúng tôi sẽ liên hệ với Quý khách để xác nhận lại thông tin và tiến hành các bước tiếp theo.\n\n")
                .append("Nếu Quý khách có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi\n")
                .append("nếu cần hỗ trợ, xin vui lòng liên hệ:\n")
                .append("• Điện thoại: 038 8888 8865\n")
                .append("• Email: support@cocoland.vn\n")
                .append("• Zalo/WeChat: @cocoland_support\n")
                .append("Một lần nữa, xin chân thành cảm ơn Quý khách đã lựa chọn COCOLAND!\n\n")
                .append("Trân trọng,\n")
                .append("CEO Nguyễn Hoàng Thái Thịnh\n")
                .append("COCOLAND Team")
                .toString();
    }

    private String buildEmailSubject(Customer c, Order o) {
        String action = switch (o.getStatus() != null ? o.getStatus() : "") {
            case "Hoan thanh" -> "Đã hoàn thành";
            case "Don huy" -> "Đã hủy";
            default -> "Xác nhận";
        };
        return "[COCOLAND] " + action + " đơn hàng #" + o.getId() + " – Cảm ơn " + c.getName();
    }

    private String friendlyStatus(String status) {
        return switch (status != null ? status : "") {
            case "Dang may" -> "Đang xử lý";
            case "Hoan thanh" -> "Đã hoàn thành";
            case "Don huy" -> "Đã hủy";
            default -> status;
        };
    }

    // -------- ZNS -------- //
    public void sendOrderZns(String phone, Order order, List<OrderDetail> details) {
        if (ZALO_ACCESS_TOKEN == null || ZALO_TEMPLATE_ID == null) return;
        String normalized = normalizePhone(phone);
        if (normalized == null) return;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("https://business.openapi.zalo.me/message/template").openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("access_token", ZALO_ACCESS_TOKEN);
            conn.setDoOutput(true);

            Map<String, Object> data = new HashMap<>();
            data.put("items", details.stream().map(d -> d.getProductType() + " x" + d.getQuantity()).collect(Collectors.joining(", ")));
            data.put("total", order.getTotal());
            data.put("paid", order.getDeposit());
            data.put("order_date", DF.format(order.getOrderDate()));
            data.put("appointment_date", DF.format(order.getDeliveryDate()));

            Map<String, Object> payload = new HashMap<>();
            payload.put("phone", normalized);
            payload.put("template_id", ZALO_TEMPLATE_ID);
            payload.put("template_data", data);
            payload.put("tracking_id", "order_" + order.getId());

            String json = new GsonBuilder().create().toJson(payload);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    code >= 400 ? conn.getErrorStream() : conn.getInputStream(), StandardCharsets.UTF_8));
            String resp = br.lines().collect(Collectors.joining());
            LOG.info("ZNS response: HTTP " + code + " - " + resp);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Send ZNS failed", e);
        }
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        String digits = phone.replaceAll("\\D", "");
        if (digits.isEmpty()) return null;
        if (digits.startsWith("0")) return "84" + digits.substring(1);
        if (!digits.startsWith("84")) return "84" + digits;
        return digits;
    }
}