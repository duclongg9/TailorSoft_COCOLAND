package model;

public class OrderDetail {
    private int id;
    private int orderId;
    private int productTypeId;
    private String productType;
    private int materialId;
    private String materialName;
    private double unitPrice;
    private int quantity;
    private String note;

    public OrderDetail() {}

    public OrderDetail(int id, int orderId, int productTypeId, String productType, int materialId, String materialName, double unitPrice, int quantity, String note) {
        this.id = id;
        this.orderId = orderId;
        this.productTypeId = productTypeId;
        this.productType = productType;
        this.materialId = materialId;
        this.materialName = materialName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.note = note;
    }
    
    public OrderDetail(int id, int orderId, String productType, int materialId, String materialName, double unitPrice, int quantity, String note) {
        this(id, orderId, 0, productType, materialId, materialName, unitPrice, quantity, note);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getProductTypeId() { return productTypeId; }
    public void setProductTypeId(int productTypeId) { this.productTypeId = productTypeId; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public int getMaterialId() { return materialId; }
    public void setMaterialId(int materialId) { this.materialId = materialId; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
