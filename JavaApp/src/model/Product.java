package model;

/**
 * Lớp này đại diện cho một Sản phẩm (tương ứng với bảng 'product' trong CSDL).
 * ĐÃ CẬP NHẬT để dùng "quantity" thay vì "stock".
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity; // SỬA LỖI: Đổi tên từ stock -> quantity
    
    // Bổ sung: Thêm cột 'unit' (đơn vị) như trong SQL của bạn
    private String unit; 

    // Constructor rỗng
    public Product() {
    }
    
    // Constructor (Hàm dựng) đầy đủ (Dùng khi ĐỌC từ CSDL)
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity; // SỬA LỖI: Đổi tên từ stock
    }
    
    // Constructor (Hàm dựng) (Dùng khi THÊM sản phẩm mới)
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity; // SỬA LỖI: Đổi tên từ stock
    }

    // Các phương thức "get"
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    
    // SỬA LỖI: Đổi tên hàm
    public int getQuantity() { return quantity; } 
    
    // (Hàm mới)
    public String getUnit() { return unit; }


    // Các phương thức "set"
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    
    // SỬA LỖI: Đổi tên hàm và tham số
    public void setQuantity(int quantity) { this.quantity = quantity; } 
    
    // (Hàm mới)
    public void setUnit(String unit) { this.unit = unit; }


    /**
     * Dùng để in thông tin sản phẩm ra cho dễ đọc (dùng trong TestConnection)
     */
    @Override
    public String toString() {
        return "Product [ID=" + id + 
               ", Ten='" + name + '\'' + 
               ", Gia=" + price + 
               ", Kho=" + quantity + ']'; // SỬA LỖI: Đổi tên từ stock
    }
}

