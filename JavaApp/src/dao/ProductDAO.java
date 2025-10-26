package dao;

import model.Product;
import utils.DatabaseConnector; // Đảm bảo import lớp kết nối

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này chịu trách nhiệm cho tất cả các thao tác CSDL
 * liên quan đến bảng 'products'.
 */
public class ProductDAO {

    /**
     * Phương thức này lấy TẤT CẢ sản phẩm từ database.
     * @return một List (danh sách) các đối tượng Product.
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        // Câu lệnh SQL để chọn tất cả sản phẩm
        String sql = "SELECT * FROM products";

        // Sử dụng try-with-resources để đảm bảo kết nối được đóng tự động
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Lặp qua từng dòng kết quả trả về từ database
            while (rs.next()) {
                // Đọc dữ liệu từ các cột
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");

                // Tạo một đối tượng Product từ dữ liệu
                Product product = new Product(id, name, price, stock);
                
                // Thêm đối tượng Product vào danh sách
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Trả về danh sách (có thể rỗng nếu không tìm thấy gì hoặc có lỗi)
        return productList;
    }

    // --- CÁC PHƯƠNG THỨC KHÁC SẼ ĐƯỢC THÊM SAU ---
    // public void addProduct(Product product) { ... }
    // public void updateProduct(Product product) { ... }
    // public void deleteProduct(int productId) { ... }
}

