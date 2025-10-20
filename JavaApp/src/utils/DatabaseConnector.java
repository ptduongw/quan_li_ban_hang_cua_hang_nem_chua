package quanlybanhang.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp này chịu trách nhiệm duy nhất cho việc kết nối đến cơ sở dữ liệu MySQL.
 * Đây là một lớp tiện ích, tất cả các lớp DAO sẽ sử dụng nó.
 */
public class DatabaseConnector {
    
    // --- BẠN CẦN THAY ĐỔI CÁC THÔNG SỐ NÀY CHO PHÙ HỢP VỚI MÁY CỦA BẠN ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quan_ly_nem_chua";
    private static final String USER = "root";
    private static final String PASS = ""; // Mật khẩu MySQL của bạn, nếu không có thì để trống
    // --------------------------------------------------------------------

    /**
     * Phương thức này tạo và trả về một đối tượng Connection.
     * @return một đối tượng Connection để tương tác với DB, hoặc null nếu kết nối thất bại.
     */
    public static Connection getConnection() {
        try {
            // Nạp driver của MySQL (đảm bảo bạn đã thêm file JAR vào project)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Tạo kết nối
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (ClassNotFoundException ex) {
            System.err.println("Lỗi: Không tìm thấy MySQL JDBC Driver! Bạn đã thêm file JAR vào thư mục Libraries chưa?");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Lỗi: Kết nối tới cơ sở dữ liệu thất bại! Kiểm tra lại DB_URL, USER, PASS.");
            ex.printStackTrace();
        }
        return null;
    }
}