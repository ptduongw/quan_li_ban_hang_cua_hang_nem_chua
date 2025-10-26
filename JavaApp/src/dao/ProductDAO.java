package dao;

import model.Product;
import utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này chịu trách nhiệm cho tất cả các thao tác CSDL
 * liên quan đến bảng 'product'.
 * ĐÃ CẬP NHẬT để dùng tên cột "quantity" (thay vì "stock").
 * ĐÃ CẬP NHẬT để dùng tên bảng "product" (thay vì "products").
 * ĐÃ CẬP NHẬT để bao gồm cột "unit".
 */
public class ProductDAO {

    /**
     * READ: Lấy TẤT CẢ sản phẩm từ database.
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM product"; 

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity") // SỬA LỖI: Đổi "stock" -> "quantity"
                );
                // Lấy thêm cột "unit" mới
                product.setUnit(rs.getString("unit"));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return productList;
    }
    
    /**
     * CREATE: Thêm một sản phẩm mới vào database.
     * @param product Đối tượng Product chứa thông tin (name, price, quantity)
     */
    public boolean addProduct(Product product) {
        // SỬA LỖI: Đổi "stock" -> "quantity" và thêm "unit"
        String sql = "INSERT INTO product(name, price, quantity, unit) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getQuantity()); // SỬA LỖI: Đổi hàm
            pstmt.setString(4, product.getUnit()); // Thêm cột "unit"

            int rowsAffected = pstmt.executeUpdate();
            
            // Lấy ID tự động tăng
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setId(rs.getInt(1)); // Cập nhật ID cho đối tượng product
                    }
                }
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * UPDATE: Cập nhật thông tin một sản phẩm dựa trên ID.
     * @param product Đối tượng Product chứa thông tin MỚI
     */
    public boolean updateProduct(Product product) {
        // SỬA LỖI: Đổi "stock" -> "quantity" và thêm "unit"
        String sql = "UPDATE product SET name = ?, price = ?, quantity = ?, unit = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getQuantity()); // SỬA LỖI: Đổi hàm
            pstmt.setString(4, product.getUnit()); // Thêm cột "unit"
            pstmt.setInt(5, product.getId()); 

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * DELETE: Xóa một sản phẩm khỏi database dựa trên ID.
     * @param productId ID của sản phẩm cần xóa
     */
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật số lượng tồn kho (dùng khi bán hàng)
     * @param conn Một kết nối CSDL (để dùng chung transaction)
     * @param productId ID sản phẩm đã bán
     * @param quantitySold Số lượng đã bán
     * @return true nếu thành công
     * @throws SQLException ném lỗi nếu thất bại (hết hàng)
     */
    public boolean updateStock(Connection conn, int productId, int quantitySold) throws SQLException {
        // SỬA LỖI: Đổi "stock" -> "quantity"
        String sql = "UPDATE product SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantitySold);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantitySold); // Đảm bảo không bán lố kho
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                // Ném lỗi này để OrderDAO có thể bắt được và rollback
                throw new SQLException("Không thể cập nhật kho: Hết hàng hoặc sản phẩm không tồn tại. ID: " + productId);
            }
            return true;
        }
    }
}

