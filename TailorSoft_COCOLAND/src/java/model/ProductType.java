package model;

public class ProductType {
    private int id;
    private String name;
    private String code;

    public ProductType() {}

    public ProductType(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    
    /**
     * Sets the textual code identifying the product type.
     *
     * @param code the code value as stored in the database
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Overloaded setter accepting numeric codes. This helps avoid
     * {@link NoSuchMethodError} when older compiled classes still
     * reference a version of this method with an {@code int}
     * parameter. The numeric value is converted to a string before
     * assignment.
     *
     * @param code the numeric representation of the code
     */
    public void setCode(int code) {
        this.code = String.valueOf(code);
    }
}
