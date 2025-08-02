package model;

public class MeasurementType {
    private int id;
    private String name;
    private String unit;
    private String bodyPart;
    private String note;

    public MeasurementType() {}

    public MeasurementType(int id, String name, String unit, String bodyPart, String note) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.bodyPart = bodyPart;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getBodyPart() { return bodyPart; }
    public void setBodyPart(String bodyPart) { this.bodyPart = bodyPart; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
