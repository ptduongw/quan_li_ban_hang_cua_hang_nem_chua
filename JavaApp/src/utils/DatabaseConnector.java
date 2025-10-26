// BẮT BUỘC: Dòng này phải là dòng đầu tiên (sửa lỗi BUILD FAILED)
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp này chịu trách nhiệm duy nhất cho việc kết nối đến cơ sở dữ liệu.
 */
public class DatabaseConnector {
    
    // --- BẠN CẦN THAY ĐỔI CÁC THÔNG SỐ NÀY ---
    // Đảm bảo tên database "data_base" đã đúng
    private static final String DB_URL = "jdbc:mysql://localhost:3306/data_base";
    // Đảm bảo username đã đúng 
    private static final String USER = "root";
    // Đảm bảo bạn đã nhập đúng mật khẩu MySQL của mình
    private static final String PASS = "611111"; // Giữ nguyên mật khẩu của bạn
    // ------------------------------------------

    /**
     * Phương thức này tạo và trả về một đối tượng Connection.
     * @return một đối tượng Connection, hoặc null nếu kết nối thất bại.
     */
    public static Connection getConnection() {
        try {
            // Nạp driver của MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Tạo kết nối
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // Nếu không ném ra ngoại lệ, nghĩa là kết nối thành công
            return conn;
            
        } catch (ClassNotFoundException ex) {
            System.err.println("Lỗi: Không tìm thấy MySQL JDBC Driver!");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Lỗi: Kết nối tới cơ sở dữ liệu thất bại! Kiểm tra DB_URL, USER, PASS.");
            ex.printStackTrace();
        }
        return null;
    }
}

