package model;

public class Measurement {
    private int id;
    private int customerId;
    private int productTypeId;
    private int measurementTypeId;
    private double value;
    private String note;

    public Measurement() {}

    public Measurement(int id, int customerId, int productTypeId, int measurementTypeId, double value, String note) {
        this.id = id;
        this.customerId = customerId;
        this.productTypeId = productTypeId;
        this.measurementTypeId = measurementTypeId;
        this.value = value;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getProductTypeId() { return productTypeId; }
    public void setProductTypeId(int productTypeId) { this.productTypeId = productTypeId; }
    public int getMeasurementTypeId() { return measurementTypeId; }
    public void setMeasurementTypeId(int measurementTypeId) { this.measurementTypeId = measurementTypeId; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
