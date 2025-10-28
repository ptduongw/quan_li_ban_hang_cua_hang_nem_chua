package model;

/**
 * Lớp này đại diện cho một Sản phẩm (ánh xạ tới bảng 'product').
 * Đã sửa lại tên cột stock -> quantity và thêm unit, note.
 */
public class Product {
    private int id;
    private String name;
    private String unit; // Đơn vị tính (ví dụ: "chục cái", "kg", "piece")
    private double price;
    private int quantity; // Đổi tên từ stock
    private String note;  // Ghi chú thêm

    // Hàm dựng đầy đủ (thường dùng khi đọc từ database)
    public Product(int id, String name, String unit, double price, int quantity, String note) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
        this.note = note;
    }

    // Hàm dựng để tạo sản phẩm mới (ID sẽ tự tăng) - **HÀM NÀY LÀ QUAN TRỌNG**
    public Product(String name, String unit, double price, int quantity, String note) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.quantity = quantity;
        this.note = note;
    }

    // Getters and Setters (Bạn có thể tự tạo bằng NetBeans: Chuột phải -> Insert Code -> Getter and Setter...)
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

    // Phương thức toString() để in thông tin sản phẩm ra cho đẹp
    @Override
    public String toString() {
        return "Product [ID=" + id + ", Ten='" + name + '\'' + ", Don vi='" + unit + '\'' +
               ", Gia=" + String.format("%,.0f", price) + ", Kho=" + quantity + ", Ghi chu='" + note + '\'' + ']';
    }
}

