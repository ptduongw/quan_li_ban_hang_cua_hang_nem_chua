package model;

public class Product {
    private int id;
    private String name;
    private String unit; //Đơn vị
    private double price;
    private int quantity;
    private String note; 

    public Product(int id, String name, String unit, double price, int quantity, String note) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
        this.note = note;
    }

    public Product(String name, String unit, double price, int quantity, String note) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Product [ID=" + id + ", Ten='" + name + '\'' + ", Don vi='" + unit + '\'' +
               ", Gia=" + String.format("%,.0f", price) + ", Kho=" + quantity + ", Ghi chu='" + note + '\'' + ']';
    }
}

