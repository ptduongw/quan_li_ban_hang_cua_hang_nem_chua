package controller;

import dao.OrderDAO;
import dao.ProductDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import quanlybanhang.model.Order;
import quanlybanhang.model.OrderDetail;
import quanlybanhang.model.Product;
import view.SalesView;

/**
 * Lớp Controller cho chức năng Bán hàng.
 */
public class SalesController {
    
    private SalesView view;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private DefaultTableModel cartTableModel;
    private List<Product> productList; // Lưu danh sách sản phẩm để truy cập nhanh

    public SalesController(SalesView view) {
        this.view = view;
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
        this.cartTableModel = (DefaultTableModel) this.view.getCartTable().getModel();
    }
    
    /**
     * Tải danh sách sản phẩm từ CSDL và hiển thị lên ComboBox.
     */
    public void loadProductsToComboBox() {
        productList = productDAO.getAllProducts();
        JComboBox<Product> comboBox = view.getProductComboBox();
        
        DefaultComboBoxModel<Product> model = new DefaultComboBoxModel<>();
        for (Product product : productList) {
            model.addElement(product);
        }
        comboBox.setModel(model);
    }
    
    /**
     * Thêm sản phẩm được chọn vào giỏ hàng (JTable).
     */
    public void addToCart() {
        Product selectedProduct = (Product) view.getProductComboBox().getSelectedItem();
        int quantity = (Integer) view.getQuantitySpinner().getValue();
        
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(view, "Số lượng phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (quantity > selectedProduct.getStock()) {
            JOptionPane.showMessageDialog(view, "Số lượng trong kho không đủ. Chỉ còn " + selectedProduct.getStock() + " sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double subtotal = selectedProduct.getPrice() * quantity;
        
        // Thêm vào bảng giỏ hàng
        cartTableModel.addRow(new Object[]{
            selectedProduct.getId(),
            selectedProduct.getName(),
            quantity,
            selectedProduct.getPrice(),
            subtotal
        });
        
        updateTotalAmount();
    }
    
    /**
     * Cập nhật tổng số tiền của hóa đơn.
     */
    private void updateTotalAmount() {
        double total = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            total += (double) cartTableModel.getValueAt(i, 4); // Cột thành tiền (subtotal)
        }
        view.getLblTotalAmount().setText(String.format("%,.0f VND", total));
    }
    
    /**
     * Xử lý việc thanh toán hóa đơn.
     */
    public void checkout() {
        int rowCount = cartTableModel.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng đang trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmation = JOptionPane.showConfirmDialog(view, "Xác nhận thanh toán hóa đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        // 1. Tạo đối tượng Order
        double totalAmount = 0;
        for (int i = 0; i < rowCount; i++) {
            totalAmount += (double) cartTableModel.getValueAt(i, 4);
        }
        Order order = new Order(0, new Date(), totalAmount);

        // 2. Tạo danh sách OrderDetail
        List<OrderDetail> details = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            int productId = (int) cartTableModel.getValueAt(i, 0);
            int quantity = (int) cartTableModel.getValueAt(i, 2);
            double price = (double) cartTableModel.getValueAt(i, 3);
            details.add(new OrderDetail(0, 0, productId, quantity, price)); // orderId và id sẽ được xử lý trong DAO
        }

        // 3. Gọi DAO để lưu vào CSDL
        // QUAN TRỌNG: Hàm addOrder trong DAO cần được thiết kế để vừa thêm hóa đơn,
        // vừa cập nhật lại số lượng tồn kho của sản phẩm trong cùng một transaction.
        boolean success = orderDAO.addOrder(order, details);
        
        if (success) {
            JOptionPane.showMessageDialog(view, "Thanh toán thành công!");
            // Xóa giỏ hàng và làm mới
            cartTableModel.setRowCount(0);
            updateTotalAmount();
            loadProductsToComboBox(); // Tải lại sản phẩm để cập nhật số lượng tồn kho
        } else {
            JOptionPane.showMessageDialog(view, "Thanh toán thất bại, đã có lỗi xảy ra.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
