package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import quanlybanhang.model.Order;
import quanlybanhang.model.OrderDetail;
import utils.DatabaseConnector;

/**
 * Lớp này xử lý các thao tác CSDL liên quan đến Hóa đơn (Order và OrderDetail).
 */
public class OrderDAO {

    /**
     * Thêm một hóa đơn mới và các chi tiết của nó vào CSDL.
     * Đây là một giao dịch (transaction) để đảm bảo tính toàn vẹn dữ liệu.
     * @param order Đối tượng Order cần thêm.
     * @param details Danh sách các OrderDetail của hóa đơn đó.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addOrder(Order order, java.util.List<OrderDetail> details) {
        String sqlOrder = "INSERT INTO orders (order_date, total_amount) VALUES (?, ?)";
        String sqlDetail = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnector.getConnection();
            // Bắt đầu một transaction, tắt chế độ tự động commit
            conn.setAutoCommit(false);

            // 1. Thêm vào bảng orders
            try (PreparedStatement pstmtOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOrder.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
                pstmtOrder.setDouble(2, order.getTotalAmount());
                pstmtOrder.executeUpdate();

                // Lấy ID của hóa đơn vừa được tạo
                try (ResultSet generatedKeys = pstmtOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);

                        // 2. Thêm vào bảng order_details với ID hóa đơn vừa lấy
                        try (PreparedStatement pstmtDetail = conn.prepareStatement(sqlDetail)) {
                            for (OrderDetail detail : details) {
                                pstmtDetail.setInt(1, orderId);
                                pstmtDetail.setInt(2, detail.getProductId());
                                pstmtDetail.setInt(3, detail.getQuantity());
                                pstmtDetail.setDouble(4, detail.getPrice());
                                pstmtDetail.addBatch(); // Thêm vào lô để xử lý nhanh hơn
                            }
                            pstmtDetail.executeBatch();
                        }
                    } else {
                        throw new SQLException("Tạo hóa đơn thất bại, không lấy được ID.");
                    }
                }
            }
            
            // Nếu mọi thứ thành công, commit transaction
            conn.commit();
            return true;

        } catch (SQLException e) {
            // Nếu có lỗi, rollback transaction
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Luôn luôn bật lại auto-commit và đóng kết nối
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
