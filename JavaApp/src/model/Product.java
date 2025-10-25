// BẮT BUỘC: Dòng này phải là dòng đầu tiên
package model;

/**
 * Lớp này đại diện cho một Sản phẩm (giống hệt cấu trúc bảng 'products')
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    // Constructor (hàm dựng)
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Các phương thức Getters và Setters
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Ghi đè phương thức toString() để hiển thị tên trong JList/JComboBox
    @Override
    public String toString() {
        return this.name + " (" + this.price + " VND)";
    }
}
