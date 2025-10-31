package controller;

import dao.ProductDAO;
import model.Product;
import java.util.List;

/**
 * Lớp này là "bộ não" logic cho Giao Diện Quản Lý Sản Phẩm (ProductManagementView).
 * Nó làm "bảo vệ" và là trung gian giữa View và ProductDAO.
 */
public class ProductController {

    private ProductDAO productDAO;

    public ProductController() {
        this.productDAO = new ProductDAO();
    }

    /**
     * Lấy danh sách tất cả sản phẩm.
     * @return Danh sách Product.
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    /**
     * Lấy một sản phẩm bằng ID.
     * @param id ID sản phẩm.
     * @return Product hoặc null nếu không tìm thấy.
     */
    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    /**
     * Xử lý logic thêm sản phẩm mới.
     * Kiểm tra dữ liệu đầu vào ("validation") trước khi gọi DAO.
     * @param name Tên sản phẩm
     * @param unit Đơn vị
     * @param price Giá
     * @param quantity Số lượng
     * @param note Ghi chú
     * @return ID của sản phẩm mới (lớn hơn 0) nếu thành công, -1 nếu thất bại.
     */
    public int addProduct(String name, String unit, double price, int quantity, String note) {
        // --- BUOC 1: Kiem tra du lieu dau vao ---
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Loi Controller: Ten san pham khong duoc de trong.");
            return -1; // Tra ve -1 (ma loi)
        }
        if (price <= 0) {
            System.err.println("Loi Controller: Gia san pham phai lon hon 0.");
            return -1;
        }
        if (quantity < 0) {
            System.err.println("Loi Controller: So luong khong duoc la so am.");
            return -1;
        }
        
        // --- BUOC 2: Tao doi tuong Product ---
        Product product = new Product(name.trim(), unit, price, quantity, note);
        
        // --- BUOC 3: Goi DAO de luu ---
        return productDAO.addProduct(product);
    }

    /**
     * Xử lý logic cập nhật sản phẩm.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updateProduct(int id, String name, String unit, double price, int quantity, String note) {
         // --- BUOC 1: Kiem tra du lieu dau vao ---
         if (id <= 0) {
             System.err.println("Loi Controller: ID san pham khong hop le.");
             return false;
         }
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Loi Controller: Ten san pham khong duoc de trong.");
            return false;
        }
        if (price <= 0) {
            System.err.println("Loi Controller: Gia san pham phai lon hon 0.");
            return false;
        }
        
        // --- BUOC 2: Tao doi tuong Product ---
        Product product = new Product(id, name.trim(), unit, price, quantity, note);
        
        // --- BUOC 3: Goi DAO de cap nhat ---
        return productDAO.updateProduct(product);
    }

    /**
     * Xử lý logic xóa sản phẩm.
     * @param id ID sản phẩm cần xóa.
     * @return true nếu xóa thành công.
     */
    public boolean deleteProduct(int id) {
        if (id <= 0) {
             System.err.println("Loi Controller: ID san pham khong hop le.");
            return false;
        }
        return productDAO.deleteProduct(id);
    }
}

