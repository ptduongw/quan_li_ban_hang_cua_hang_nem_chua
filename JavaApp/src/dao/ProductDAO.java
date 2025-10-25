// BẮT BUỘC: Dòng này phải là dòng đầu tiên
package dao;

// Import các lớp cần thiết
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product; // Import lớp Product từ gói model
import utils.DatabaseConnector; // Import lớp kết nối

/**
 * Lớp này chịu trách nhiệm truy vấn cơ sở dữ liệu cho bảng 'products'.
 */
public class ProductDAO {

    // Phương thức để lấy tất cả sản phẩm từ database
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        // Câu lệnh SQL để chọn tất cả sản phẩm
        String sql = "SELECT * FROM products"; 

        // Sử dụng try-with-resources để tự động đóng kết nối
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Lặp qua từng dòng kết quả trả về
            while (rs.next()) {
                // Tạo một đối tượng Product từ dữ liệu
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
                // Thêm sản phẩm vào danh sách
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productList; // Trả về danh sách
    }
    
    // (Sau này chúng ta sẽ thêm các hàm add, update, delete ở đây)
    // public boolean addProduct(Product product) { ... }
    // public boolean updateProduct(Product product) { ... }
    // public boolean deleteProduct(int productId) { ... }
}

