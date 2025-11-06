package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import utils.DatabaseConnector;

/**
 * Lớp này xử lý tất cả các thao tác CRUD (Create, Read, Update, Delete)
 * liên quan đến bảng 'product' trong database.
 * Đã sửa lại để khớp với Product.java mới (quantity, unit, note, constructors).
 */
public class ProductDAO {

    /**
     * Lấy tất cả sản phẩm từ database.
     * @return Danh sách các đối tượng Product, hoặc danh sách rỗng nếu không có.
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        // Sửa lại câu SQL để dùng tên bảng 'product'
        String sql = "SELECT * FROM product";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) {
                 System.err.println("Lỗi: Không thể kết nối đến database.");
                 return productList; // Trả về danh sách rỗng
            }
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Sử dụng hàm dựng đầy đủ mới của Product
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unit"), // Lấy thêm cột unit
                        rs.getDouble("price"),
                        rs.getInt("quantity"), // Sửa lại tên cột từ stock -> quantity
                        rs.getString("note")   // Lấy thêm cột note
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi ra console
        } finally {
            // Đóng các resource để tránh rò rỉ bộ nhớ
            closeResources(conn, pstmt, rs);
        }
        return productList;
    }

    /**
     * Tìm sản phẩm theo ID.
     * @param id ID của sản phẩm cần tìm.
     * @return Đối tượng Product nếu tìm thấy, ngược lại trả về null.
     */
    public Product getProductById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Product product = null;

        try {
            conn = DatabaseConnector.getConnection();
             if (conn == null) return null;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("note")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm sản phẩm theo ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return product;
    }

    /**
     * Thêm một sản phẩm mới vào database.
     * @param product Đối tượng Product chứa thông tin sản phẩm mới (ID có thể bỏ qua).
     * @return ID của sản phẩm vừa được thêm vào, hoặc -1 nếu thất bại.
     */
    public int addProduct(Product product) {
        // Sửa câu SQL để khớp với các cột mới
        String sql = "INSERT INTO product (name, unit, price, quantity, note) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int generatedId = -1;

        try {
            conn = DatabaseConnector.getConnection();
             if (conn == null) return -1;
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getUnit()); // Thêm unit
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity()); // Sửa stock -> quantity
            pstmt.setString(5, product.getNote());   // Thêm note

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, generatedKeys); // Đóng cả generatedKeys
        }
        return generatedId;
    }

    /**
     * Cập nhật thông tin một sản phẩm trong database.
     * @param product Đối tượng Product chứa thông tin đã được cập nhật (phải có ID).
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, unit = ?, price = ?, quantity = ?, note = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
             if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getUnit());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getQuantity());
            pstmt.setString(5, product.getNote());
            pstmt.setInt(6, product.getId()); // ID ở cuối cùng cho mệnh đề WHERE

            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        return success;
    }

    /**
     * Xóa một sản phẩm khỏi database theo ID.
     * @param id ID của sản phẩm cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
             if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        return success;
    }

    // --- Hàm tiện ích để đóng tài nguyên ---
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) { /* Bỏ qua */ }
        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) { /* Bỏ qua */ }
        // Không đóng Connection ở đây, lớp gọi hàm này sẽ quản lý
        // try {
        //     if (conn != null) conn.close();
        // } catch (SQLException e) { /* Bỏ qua */ }
    }
}

