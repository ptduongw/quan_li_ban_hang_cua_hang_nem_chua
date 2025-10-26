package Test; // Gói của bạn tên là "Test"

import dao.ProductDAO;
import model.Product;
import utils.DatabaseConnector;

import java.sql.Connection;
import java.util.List;

/**
 * Lớp này dùng để kiểm tra kết nối và các chức năng của DAO.
 * ĐÃ CẬP NHẬT để khớp với ProductDAO (dùng quantity và unit)
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("--- BƯỚC 1: KIỂM TRA KẾT NỐI DATABASE ---");

        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn != null) {
                System.out.println("KET NOI THANH CONG!");
                System.out.println("Ban da san sang de lam viec voi database 'shop_management'.");
                System.out.println("-------------------------------------------------");
                
                // Sau khi kết nối thành công, tự động chạy BƯỚC 2
                testProductDAO(conn);

            } else {
                System.err.println("KET NOI THAT BAI!");
            }
        } catch (Exception e) {
            System.err.println("KET NOI THAT BAI!");
            e.printStackTrace();
        }
    }
    
    /**
     * Phương thức này dùng để kiểm tra tất cả 4 chức năng của ProductDAO
     */
    private static void testProductDAO(Connection conn) {
        ProductDAO productDAO = new ProductDAO();
        
        // -----------------------------------------------------------------
        System.out.println("\n--- BƯỚC 2: TEST READ (getAllProducts) ---");
        List<Product> productList = productDAO.getAllProducts();
        
        if (productList.isEmpty()) {
            System.err.println("Lỗi: Không tìm thấy sản phẩm nào, hoặc có lỗi khi lấy dữ liệu.");
        } else {
            System.out.println("Đã tìm thấy " + productList.size() + " sản phẩm:");
            for (Product p : productList) {
                System.out.println(p.toString()); // In ra từng sản phẩm
            }
        }
        System.out.println("-------------------------------------------------");
        
        // -----------------------------------------------------------------
        System.out.println("\n--- BƯỚC 3: TEST CREATE (addProduct) ---");
        // Hàm dựng (String, double, int) vẫn tồn tại
        Product newProduct = new Product("Bánh Mì Test", 15000.0, 50); 
        
        // CẬP NHẬT: Thêm "unit"
        newProduct.setUnit("cai"); 
        
        boolean addSuccess = productDAO.addProduct(newProduct);
        
        if (addSuccess && newProduct.getId() > 0) {
            System.out.println("Đã thêm thành công sản phẩm mới:");
            System.out.println(newProduct.toString());
            
            // Nếu thêm thành công, chạy BƯỚC 4
            testUpdateAndDelete(productDAO, newProduct);
            
        } else {
            System.err.println("Lỗi: Thêm sản phẩm mới thất bại!");
        }
    }
    
    /**
     * Phương thức này kiểm tra Update và Delete
     */
    private static void testUpdateAndDelete(ProductDAO productDAO, Product addedProduct) {
        // Lưu lại ID của sản phẩm vừa thêm
        int newId = addedProduct.getId();
        
        // -----------------------------------------------------------------
        System.out.println("\n--- BƯỚC 4: TEST UPDATE (updateProduct) ---");
        
        // Cập nhật giá và số lượng
        addedProduct.setPrice(20000.0); // Tăng giá lên 20000
        
        // SỬA LỖI: Đổi setStock(40) -> setQuantity(40)
        addedProduct.setQuantity(40); // Giảm kho còn 40
        
        boolean updateSuccess = productDAO.updateProduct(addedProduct);
        
        if (updateSuccess) {
            System.out.println("Đã cập nhật thành công sản phẩm ID: " + newId);
        } else {
            System.err.println("Lỗi: Cập nhật sản phẩm thất bại!");
        }
        
        // -----------------------------------------------------------------
        System.out.println("\n--- BƯỚC 5: TEST DELETE (deleteProduct) ---");
        
        boolean deleteSuccess = productDAO.deleteProduct(newId);
        
        if (deleteSuccess) {
            System.out.println("Đã xóa thành công sản phẩm 'Bánh Mì Test' (ID: " + newId + ")");
        } else {
            System.err.println("Lỗi: Xóa sản phẩm thất bại!");
        }
        
        System.out.println("\n--- KIỂM TRA HOÀN TẤT ---");
    }
}

