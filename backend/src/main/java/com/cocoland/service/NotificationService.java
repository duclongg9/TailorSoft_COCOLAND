package com.cocoland.service;

import com.cocoland.entity.Customer;
import com.cocoland.entity.Order;
import com.cocoland.entity.OrderDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${zalo.access-token:}")
    private String zaloToken;

    @Value("${zalo.template-id:}")
    private String zaloTemplateId;

    private static final DecimalFormat CURRENCY = new DecimalFormat("#,###");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void notifyOrder(Customer customer, Order order, List<OrderDetail> details) {
        if (customer != null && customer.getEmail() != null && !customer.getEmail().isBlank()) {
            sendEmail(customer, order, details);
        }
        if (customer != null) {
            sendZns(customer.getPhone(), order, details);
        }
    }

    private void sendEmail(Customer customer, Order order, List<OrderDetail> details) {
        if (fromEmail == null || fromEmail.isBlank()) {
            log.warn("Email not configured, skip sending");
            return;
        }
        try {
            String items = details.stream()
                    .map(d -> "• " + d.getProductType() + " × " + d.getQuantity())
                    .collect(Collectors.joining("\n"));
            double remaining = (order.getTotal() != null ? order.getTotal() : 0)
                             - (order.getDeposit() != null ? order.getDeposit() : 0);

            String status = switch (order.getStatus() != null ? order.getStatus() : "") {
                case "Hoan thanh" -> "Đã hoàn thành";
                case "Don huy" -> "Đã hủy";
                default -> "Đang may";
            };

            String body = "Kính gửi anh/chị " + customer.getName() + ",\n\n"
                    + "Cảm ơn Quý khách đã tin tưởng và đặt hàng tại COCOLAND.\n\n"
                    + "Thông tin đơn hàng:\n"
                    + "Mã đơn: #" + order.getId() + "\n"
                    + "Sản phẩm:\n" + items + "\n"
                    + "Ngày đặt: " + (order.getOrderDate() != null ? order.getOrderDate().format(DF) : "-") + "\n"
                    + "Ngày giao dự kiến: " + (order.getDeliveryDate() != null ? order.getDeliveryDate().format(DF) : "-") + "\n"
                    + "Tổng tiền: " + CURRENCY.format(order.getTotal()) + " VND\n"
                    + "Đã cọc: " + CURRENCY.format(order.getDeposit()) + " VND\n"
                    + "Còn lại: " + CURRENCY.format(remaining) + " VND\n"
                    + "Trạng thái: " + status + "\n\n"
                    + "Liên hệ: 038 8888 8865 | support@cocoland.vn\n\n"
                    + "Trân trọng,\nCOCOLAND Team";

            String actionWord = switch (order.getStatus() != null ? order.getStatus() : "") {
                case "Hoan thanh" -> "Hoàn thành";
                case "Don huy" -> "Hủy";
                default -> "Xác nhận";
            };
            String subject = "[COCOLAND] " + actionWord + " đơn hàng #" + order.getId() + " – " + customer.getName();

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(customer.getEmail());
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            log.info("Email sent to {}", customer.getEmail());
        } catch (Exception e) {
            log.warn("Failed to send email: {}", e.getMessage());
        }
    }

    private void sendZns(String phone, Order order, List<OrderDetail> details) {
        if (zaloToken == null || zaloToken.isBlank() || zaloTemplateId == null || zaloTemplateId.isBlank()) return;
        String normalized = normalizePhone(phone);
        if (normalized == null) return;
        try {
            OkHttpClient client = new OkHttpClient();
            String items = details.stream()
                    .map(d -> d.getProductType() + " x" + d.getQuantity())
                    .collect(Collectors.joining(", "));
            String json = """
                {
                  "phone": "%s",
                  "template_id": "%s",
                  "template_data": {
                    "items": "%s",
                    "total": "%s",
                    "paid": "%s",
                    "order_date": "%s",
                    "appointment_date": "%s"
                  },
                  "tracking_id": "order_%d"
                }
                """.formatted(normalized, zaloTemplateId, items,
                    CURRENCY.format(order.getTotal()), CURRENCY.format(order.getDeposit()),
                    order.getOrderDate() != null ? order.getOrderDate().format(DF) : "-",
                    order.getDeliveryDate() != null ? order.getDeliveryDate().format(DF) : "-",
                    order.getId());
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request req = new Request.Builder()
                    .url("https://business.openapi.zalo.me/message/template")
                    .addHeader("access_token", zaloToken)
                    .post(body)
                    .build();
            try (Response resp = client.newCall(req).execute()) {
                log.info("ZNS response: {}", resp.code());
            }
        } catch (Exception e) {
            log.warn("Failed to send ZNS: {}", e.getMessage());
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
