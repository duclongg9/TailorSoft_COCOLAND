package model;

public class Measurement {
    private int id;
    private int customerId;
    private String customerName;
    private int productTypeId;
    private String productTypeName;
    private int measurementTypeId;
    private String measurementTypeName;
    private double value;
    private String note;
    private int orderDetailId;

    public Measurement() {}

    public Measurement(int id, int customerId, String customerName, int productTypeId,
                       String productTypeName, int measurementTypeId, String measurementTypeName,
                       double value, String note, int orderDetailId) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productTypeId = productTypeId;
        this.productTypeName = productTypeName;
        this.measurementTypeId = measurementTypeId;
        this.measurementTypeName = measurementTypeName;
        this.value = value;
        this.note = note;
        this.orderDetailId = orderDetailId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public int getProductTypeId() { return productTypeId; }
    public void setProductTypeId(int productTypeId) { this.productTypeId = productTypeId; }
    public String getProductTypeName() { return productTypeName; }
    public void setProductTypeName(String productTypeName) { this.productTypeName = productTypeName; }
    public int getMeasurementTypeId() { return measurementTypeId; }
    public void setMeasurementTypeId(int measurementTypeId) { this.measurementTypeId = measurementTypeId; }
    public String getMeasurementTypeName() { return measurementTypeName; }
    public void setMeasurementTypeName(String measurementTypeName) { this.measurementTypeName = measurementTypeName; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }
}
