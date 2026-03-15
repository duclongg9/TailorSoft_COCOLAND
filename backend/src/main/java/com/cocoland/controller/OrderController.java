package com.cocoland.controller;

import com.cocoland.entity.*;
import com.cocoland.repository.*;
import com.cocoland.service.FileService;
import com.cocoland.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepo;
    private final OrderDetailRepository detailRepo;
    private final CustomerRepository customerRepo;
    private final MeasurementRepository measurementRepo;
    private final MaterialRepository materialRepo;
    private final NotificationService notificationService;
    private final FileService fileService;

    // =============== LIST ===============
    @GetMapping
    public List<Order> list(@RequestParam(required = false) String status) {
        List<Order> orders = (status != null && !status.isBlank())
                ? orderRepo.findByStatus(status)
                : orderRepo.findAll();
        // Enrich với thông tin khách
        orders.forEach(this::enrich);
        return orders;
    }

    // =============== GET ONE ===============
    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Integer id) {
        return orderRepo.findById(id).map(o -> ResponseEntity.ok(enrich(o)))
                .orElse(ResponseEntity.notFound().build());
    }

    // =============== CREATE ===============
    @PostMapping
    @Transactional
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequest req) {
        if (req.getDeposit() != null && req.getTotal() != null && req.getDeposit() > req.getTotal()) {
            return ResponseEntity.badRequest().build();
        }
        Order order = new Order();
        order.setCustomerId(req.getCustomerId());
        order.setOrderDate(req.getOrderDate());
        order.setDeliveryDate(req.getDeliveryDate());
        order.setStatus(req.getStatus() != null ? req.getStatus() : "Dang may");
        order.setTotal(req.getTotal());
        order.setDeposit(req.getDeposit());

        order = orderRepo.save(order);

        // Save order details + measurements
        if (req.getDetails() != null) {
            for (CreateOrderRequest.DetailItem item : req.getDetails()) {
                OrderDetail d = new OrderDetail();
                d.setOrderId(order.getId());
                d.setProductType(item.getProductType());
                d.setMaterialId(item.getMaterialId());
                d.setMaterialName(item.getMaterialName());
                d.setUnitPrice(item.getUnitPrice());
                d.setQuantity(item.getQuantity());
                d.setNote(item.getNote());
                d = detailRepo.save(d);

                // Decrease material stock if specified
                if (item.getMaterialId() != null && item.getUsedQuantity() != null && item.getUsedQuantity() > 0) {
                    materialRepo.findById(item.getMaterialId()).ifPresent(mat -> {
                        mat.setQuantity(Math.max(0, mat.getQuantity() - item.getUsedQuantity()));
                        materialRepo.save(mat);
                    });
                }

                // Save measurements
                if (item.getMeasurements() != null) {
                    final Integer detailId = d.getId();
                    for (CreateOrderRequest.MeasurementItem mi : item.getMeasurements()) {
                        Measurement m = new Measurement();
                        m.setCustomerId(req.getCustomerId());
                        m.setProductTypeId(mi.getProductTypeId());
                        m.setMeasurementTypeId(mi.getMeasurementTypeId());
                        m.setValue(mi.getValue());
                        m.setOrderDetailId(detailId);
                        measurementRepo.save(m);
                    }
                }
            }
        }

        // Send notification
        Customer customer = customerRepo.findById(order.getId()).orElse(null);
        if (req.getCustomerId() != null) {
            customer = customerRepo.findById(req.getCustomerId()).orElse(null);
        }
        List<OrderDetail> details = detailRepo.findByOrderId(order.getId());
        notificationService.notifyOrder(customer, order, details);

        return ResponseEntity.ok(enrich(order));
    }

    // =============== UPDATE STATUS ===============
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return orderRepo.findById(id).map(o -> {
            o.setStatus(body.get("status"));
            orderRepo.save(o);
            // Gửi email khi đổi trạng thái
            customerRepo.findById(o.getCustomerId()).ifPresent(c -> {
                List<OrderDetail> details = detailRepo.findByOrderId(id);
                notificationService.notifyOrder(c, o, details);
            });
            return ResponseEntity.ok(enrich(o));
        }).orElse(ResponseEntity.notFound().build());
    }

    // =============== UPDATE AMOUNT ===============
    @PatchMapping("/{id}/amount")
    public ResponseEntity<Order> updateAmount(
            @PathVariable Integer id,
            @RequestBody Map<String, Double> body) {
        return orderRepo.findById(id).map(o -> {
            if (body.containsKey("total")) o.setTotal(body.get("total"));
            if (body.containsKey("deposit")) o.setDeposit(body.get("deposit"));
            return ResponseEntity.ok(enrich(orderRepo.save(o)));
        }).orElse(ResponseEntity.notFound().build());
    }

    // =============== UPDATE DELIVERY DATE ===============
    @PatchMapping("/{id}/delivery-date")
    public ResponseEntity<Order> updateDeliveryDate(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return orderRepo.findById(id).map(o -> {
            if (body.containsKey("deliveryDate")) {
                try {
                    o.setDeliveryDate(java.time.LocalDate.parse(body.get("deliveryDate")));
                } catch (Exception ignored) {}
            }
            return ResponseEntity.ok(enrich(orderRepo.save(o)));
        }).orElse(ResponseEntity.notFound().build());
    }

    // =============== UPLOAD IMAGES ===============
    @PostMapping("/{id}/upload-deposit")
    public ResponseEntity<Order> uploadDeposit(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) throws IOException {
        return orderRepo.findById(id).map(o -> {
            try {
                fileService.delete(o.getDepositImage());
                o.setDepositImage(fileService.save(file));
                return ResponseEntity.ok(orderRepo.save(o));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/upload-full")
    public ResponseEntity<Order> uploadFull(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) throws IOException {
        return orderRepo.findById(id).map(o -> {
            try {
                fileService.delete(o.getFullImage());
                o.setFullImage(fileService.save(file));
                return ResponseEntity.ok(orderRepo.save(o));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // =============== RESEND EMAIL ===============
    @PostMapping("/{id}/send-email")
    public ResponseEntity<Void> sendEmail(@PathVariable Integer id) {
        return orderRepo.findById(id).map(o -> {
            customerRepo.findById(o.getCustomerId()).ifPresent(c -> {
                List<OrderDetail> details = detailRepo.findByOrderId(id);
                notificationService.notifyOrder(c, o, details);
            });
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // =============== ORDER DETAILS ===============
    @GetMapping("/{id}/details")
    public ResponseEntity<List<OrderDetail>> details(@PathVariable Integer id) {
        if (!orderRepo.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(detailRepo.findByOrderId(id));
    }

    @PutMapping("/details/{detailId}")
    public ResponseEntity<OrderDetail> updateDetail(
            @PathVariable Integer detailId,
            @RequestBody OrderDetail body) {
        return detailRepo.findById(detailId).map(d -> {
            d.setProductType(body.getProductType());
            d.setMaterialId(body.getMaterialId());
            d.setMaterialName(body.getMaterialName());
            d.setUnitPrice(body.getUnitPrice());
            d.setQuantity(body.getQuantity());
            d.setNote(body.getNote());
            return ResponseEntity.ok(detailRepo.save(d));
        }).orElse(ResponseEntity.notFound().build());
    }

    // =============== HELPERS ===============
    private Order enrich(Order o) {
        if (o.getCustomerId() != null) {
            customerRepo.findById(o.getCustomerId()).ifPresent(c -> {
                o.setCustomerName(c.getName());
                o.setCustomerPhone(c.getPhone());
                o.setCustomerEmail(c.getEmail());
                o.setCustomerAddress(c.getAddress());
            });
        }
        return o;
    }

    // =============== REQUEST BODY ===============
    @lombok.Data
    public static class CreateOrderRequest {
        private Integer customerId;
        private java.time.LocalDate orderDate;
        private java.time.LocalDate deliveryDate;
        private String status;
        private Double total;
        private Double deposit;
        private List<DetailItem> details;

        @lombok.Data
        public static class DetailItem {
            private String productType;
            private Integer materialId;
            private String materialName;
            private Double unitPrice;
            private Integer quantity;
            private Double usedQuantity;
            private String note;
            private List<MeasurementItem> measurements;
        }

        @lombok.Data
        public static class MeasurementItem {
            private Integer productTypeId;
            private Integer measurementTypeId;
            private Double value;
        }
    }
}
