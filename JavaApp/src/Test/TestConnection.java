// Đảm bảo package name ở đây khớp với cây thư mục của bạn
package Test; 

// SỬA LỖI: Import lớp DatabaseConnector từ gói "utils" (gói ngang cấp)
import utils.DatabaseConnector;
import java.sql.Connection;

/**
 * Đây là lớp dùng để kiểm tra kết nối tới database.
 * Nó không phải là phần chính của ứng dụng, chỉ dùng để test.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("Dang thu ket noi den Database...");
        
        // Goi phuong thuc getConnection() tu lop DatabaseConnector
        Connection conn = DatabaseConnector.getConnection();
        
        // Kiem tra ket qua
        if (conn != null) {
            System.out.println("==========================================");
            System.out.println("KET NOI THANH CONG!");
            System.out.println("Ban da san sang de lam viec voi database.");
            System.out.println("==========================================");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("------------------------------------------");
            System.err.println("KET NOI THAT BAI!");
            System.err.println("Vui long kiem tra lai DB_URL, USER, va PASS trong file DatabaseConnector.java");
            System.err.println("------------------------------------------");
        }
    }
}
