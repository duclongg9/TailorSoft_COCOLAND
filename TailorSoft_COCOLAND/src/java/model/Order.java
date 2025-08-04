package model;

import java.util.Date;

public class Order {
    private int id;
    private int customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Date orderDate;
    private Date deliveryDate;
    private String status;
    private double total;
    private double deposit;
    private String productType;
    private String depositImage;
    private String fullImage;

    public Order() {}

    public Order(int id, int customerId, Date orderDate, Date deliveryDate, String status, double total, double deposit) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
        this.total = total;
        this.deposit = deposit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public double getDeposit() { return deposit; }
    public void setDeposit(double deposit) { this.deposit = deposit; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getDepositImage() { return depositImage; }
    public void setDepositImage(String depositImage) { this.depositImage = depositImage; }
    public String getFullImage() { return fullImage; }
    public void setFullImage(String fullImage) { this.fullImage = fullImage; }
}
