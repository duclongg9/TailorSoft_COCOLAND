package model;

public class Material {
    private int id;
    private String name;
    private String color;
    private String origin;
    private double price;
    private double quantity;

    public Material() {}

    public Material(int id, String name, String color, String origin, double price, double quantity) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.origin = origin;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}
