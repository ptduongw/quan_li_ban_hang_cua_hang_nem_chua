package Test;

// Import các lớp cần thiết
import dao.ProductDAO;
import model.Product;
import utils.DatabaseConnector;
import java.sql.Connection;
import java.util.List;

/**
 * Lớp này dùng để kiểm tra kết nối VÀ kiểm tra các chức năng DAO.
 */
public class TestConnection {

    public static void main(String[] args) {
        
        System.out.println("--- BƯỚC 1: KIỂM TRA KẾT NỐI DATABASE ---");
        
        // Bước 1: Kiểm tra kết nối
        // Chúng ta gọi getConnection() một lần để đảm bảo nó hoạt động
        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn != null) {
                System.out.println("KET NOI THANH CONG!");
                System.out.println("Ban da san sang de lam viec voi database.");
                System.out.println("----------------------------------------");
                
                // Bước 2: Nếu kết nối thành công, kiểm tra DAO
                System.out.println("--- BƯỚC 2: KIỂM TRA ProductDAO.getAllProducts() ---");
                
                // Tạo một đối tượng ProductDAO
                ProductDAO productDAO = new ProductDAO();
                
                // Gọi phương thức để lấy tất cả sản phẩm
                List<Product> productList = productDAO.getAllProducts();
                
                // Kiểm tra kết quả
                if (productList.isEmpty()) {
                    System.err.println("Lỗi: Không tìm thấy sản phẩm nào, hoặc có lỗi khi lấy dữ liệu.");
                } else {
                    System.out.println("Tim thay " + productList.size() + " san pham:");
                    // In từng sản phẩm ra
                    for (Product p : productList) {
                        System.out.println(p); // Gọi phương thức toString() của Product
                    }
                }
                
            } else {
                System.err.println("KET NOI THAT BAI!");
                System.err.println("Vui long kiem tra lai DB_URL, USER, va PASS trong file DatabaseConnector.java");
            }
        } catch (Exception e) {
            System.err.println("Da xay ra loi ngoai le!");
            e.printStackTrace();
        }
    }
}

