package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp này chịu trách nhiệm duy nhất cho việc kết nối đến cơ sở dữ liệu.
 * ĐÃ CẬP NHẬT để kết nối tới "shop_management"
 */
public class DatabaseConnector {
    
    // --- BẠN CẦN KIỂM TRA LẠI CÁC THÔNG SỐ NÀY ---
    
    // SỬA LỖI: Đổi "data_base" thành "shop_management"
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shop_management";
    
    // Dựa trên ảnh, bạn đang dùng user 'root'. Giữ nguyên.
    private static final String USER = "root";
    
    // Đảm bảo bạn đã nhập đúng mật khẩu MySQL của mình
    private static final String PASS = "611111"; // Giữ nguyên mật khẩu của bạn
    // ------------------------------------------

    /**
     * Phương thức này tạo và trả về một đối tượng Connection.
     * @return một đối tượng Connection để tương tác với DB, hoặc null nếu kết nối thất bại.
     */
    public static Connection getConnection() {
        try {
            // Nạp driver của MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Tạo kết nối
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // System.out.println("Kết nối tới CSDL 'shop_management' thành công!");
            return conn;
        } catch (ClassNotFoundException ex) {
            System.err.println("Lỗi: Không tìm thấy MySQL JDBC Driver!");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Lỗi: Kết nối tới cơ sở dữ liệu thất bại!");
            ex.printStackTrace();
        }
        return null;
    }
}

