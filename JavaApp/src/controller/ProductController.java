// BẮT BUỘC: Dòng này phải là dòng đầu tiên
package controller;

// Import các lớp mà controller này sẽ cần
import dao.ProductDAO;
// (Sau này chúng ta sẽ cần import model.Product, java.util.List...)
// import model.Product;
// import java.util.List;

/**
 * Lớp này là bộ não xử lý logic cho Quản lý Sản phẩm.
 * (Hiện tại chỉ cần khai báo lớp để hết lỗi build)
 */
public class ProductController {
    
    // Controller sẽ cần một đối tượng DAO để truy cập CSDL
    private ProductDAO productDAO;

    public ProductController() {
        // Khởi tạo đối tượng DAO khi Controller được tạo ra
        this.productDAO = new ProductDAO();
    }

    // (Các hàm logic như lấy danh sách sản phẩm, thêm, sửa, xóa sẽ ở đây)
    // public List<Product> getAllProducts() {
    //     return this.productDAO.getAllProducts();
    // }
}

