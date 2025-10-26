package model;

/**
 * Lớp này đại diện cho một Sản phẩm (tương ứng với bảng 'products' trong CSDL).
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    // Constructor (Hàm dựng) rỗng (cần cho một số thư viện sau này)
    public Product() {
    }
    
    // Constructor (Hàm dựng) đầy đủ
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Các phương thức "get"
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // Các phương thức "set" (Chúng ta sẽ cần sau này)
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * PHƯƠNG THỨC QUAN TRỌNG:
     * Ghi đè phương thức toString() để in thông tin sản phẩm ra cho đẹp.
     * File TestConnection.java sẽ gọi phương thức này.
     */
    @Override
    public String toString() {
        return "Product [ID=" + id + 
               ", Ten='" + name + '\'' + 
               ", Gia=" + price + 
               ", Kho=" + stock + ']';
    }
}

