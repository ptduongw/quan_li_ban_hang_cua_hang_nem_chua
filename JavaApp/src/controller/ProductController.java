package controller;

import dao.ProductDAO;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import quanlybanhang.model.Product;
import view.ProductManagementView;

/**
 * Lớp này là cầu nối giữa View (Giao diện) và DAO (Dữ liệu)
 * cho chức năng Quản lý sản phẩm.
 */
public class ProductController {
    
    private ProductDAO productDAO;
    private ProductManagementView view;
    private DefaultTableModel tableModel;

    public ProductController(ProductManagementView view) {
        this.view = view;
        this.productDAO = new ProductDAO();
        // Lấy model của JTable từ view
        this.tableModel = (DefaultTableModel) this.view.getProductTable().getModel();
    }
    
    /**
     * Tải toàn bộ sản phẩm từ CSDL và hiển thị lên JTable.
     */
    public void loadProducts() {
        // Xóa tất cả các hàng cũ trong bảng
        tableModel.setRowCount(0);
        
        List<Product> productList = productDAO.getAllProducts();
        for (Product product : productList) {
            // Thêm một hàng mới vào bảng với dữ liệu của sản phẩm
            tableModel.addRow(new Object[]{
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
            });
        }
    }
    
    /**
     * Xử lý sự kiện khi người dùng nhấn nút "Thêm".
     */
    public void addProduct() {
        try {
            // Lấy thông tin từ các JTextField trong view
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());
            
            // Kiểm tra thông tin đầu vào
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Tên sản phẩm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Product product = new Product(0, name, price, stock); // id là 0 vì CSDL sẽ tự động tăng
            
            if (productDAO.addProduct(product)) {
                JOptionPane.showMessageDialog(view, "Thêm sản phẩm thành công!");
                loadProducts(); // Tải lại danh sách sản phẩm để cập nhật bảng
                clearFields(); // Xóa trắng các ô nhập liệu
            } else {
                JOptionPane.showMessageDialog(view, "Thêm sản phẩm thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Giá và số lượng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xử lý sự kiện khi người dùng nhấn nút "Sửa".
     */
    public void updateProduct() {
         try {
            // Lấy thông tin từ các JTextField trong view
            int id = Integer.parseInt(view.getTxtId().getText());
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());
            
            Product product = new Product(id, name, price, stock);
            
            if (productDAO.updateProduct(product)) {
                JOptionPane.showMessageDialog(view, "Cập nhật sản phẩm thành công!");
                loadProducts(); 
                clearFields();
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật sản phẩm thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một sản phẩm để sửa và đảm bảo các trường số là hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xử lý sự kiện khi người dùng nhấn nút "Xóa".
     */
    public void deleteProduct() {
        try {
            int id = Integer.parseInt(view.getTxtId().getText());
            
            // Hiển thị hộp thoại xác nhận
            int confirmation = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirmation == JOptionPane.YES_OPTION) {
                if (productDAO.deleteProduct(id)) {
                    JOptionPane.showMessageDialog(view, "Xóa sản phẩm thành công!");
                    loadProducts();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa sản phẩm thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(view, "Vui lòng chọn một sản phẩm để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xóa trắng các ô nhập liệu.
     */
    public void clearFields() {
        view.getTxtId().setText("");
        view.getTxtName().setText("");
        view.getTxtPrice().setText("");
        view.getTxtStock().setText("");
    }
}
