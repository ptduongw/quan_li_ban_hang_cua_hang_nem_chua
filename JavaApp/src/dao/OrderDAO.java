package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import model.Order;
import model.OrderDetail;
import model.Product; 
import utils.DatabaseConnector;

public class OrderDAO {

    /**
     * Thêm một đơn hàng mới và các chi tiết của nó vào database.
     * Đồng thời cập nhật số lượng tồn kho của các sản phẩm liên quan.
     * Sử dụng Transaction để đảm bảo tính toàn vẹn dữ liệu.
     * @param order Đối tượng Order chứa thông tin đơn hàng và danh sách OrderDetail.
     * @return ID của đơn hàng vừa được tạo, hoặc -1 nếu thất bại.
     */
    //Thêm đơn hàng
    public int addOrder(Order order) {
        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psDetail = null;
        PreparedStatement psUpdateStock = null;
        ResultSet generatedKeys = null;
        int orderId = -1;

        //Thêm đơn hàng vào bảng order_table
        String sqlOrder = "INSERT INTO order_table (customer_id, order_date, total_amount) VALUES (?, ?, ?)";
        //Thêm chi tiết đơn hàng vào bảng order_detail
        String sqlDetail = "INSERT INTO order_detail (order_id, product_id, quantity, sale_price) VALUES (?, ?, ?, ?)";
        //Cập nhật số lượng sản phẩm trong kho
        String sqlUpdateStock = "UPDATE product SET quantity = quantity - ? WHERE id = ? AND quantity >= ?"; 

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) {
                System.err.println("Lỗi: Không thể kết nối đến database khi thêm Order.");
                return -1;
            }

            // --- BẮT ĐẦU TRANSACTION ---
            conn.setAutoCommit(false); // Tắt tự động commit

            //1.Thêm dữ liệu vào bảng order_table
            psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
          
            if (order.getCustomerId() > 0) {
                 psOrder.setInt(1, order.getCustomerId());
            } else {
                 psOrder.setNull(1, java.sql.Types.INTEGER); //customerId = Null cho khách lần đầu mua
            }
            //Ghi lại ngày đặt hàng
            psOrder.setTimestamp(2, new java.sql.Timestamp(order.getOrderDate() != null ? order.getOrderDate().getTime() : System.currentTimeMillis()));
            psOrder.setDouble(3, order.getTotalAmount());

            int affectedRowsOrder = psOrder.executeUpdate();

            if (affectedRowsOrder == 0) {
                conn.rollback(); //Hoàn tác nếu không thêm được order
                System.err.println("Lỗi: Không thể thêm Order vào order_table.");
                return -1;
            }

            //Lấy ID của Order vừa tạo
            generatedKeys = psOrder.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                System.err.println("Lỗi: Không lấy được ID của Order vừa tạo.");
                return -1;
            }

            //Kiểm tra xem có chi tiết sanr phẩm đơn hàng không
            if (order.getDetails() == null || order.getDetails().isEmpty()) {
                 conn.rollback();
                 System.err.println("Lỗi: Đơn hàng không có chi tiết sản phẩm nào.");
                 return -1;
            }

            //2.Thêm đơn hàng đã thanh toán vào bảng order_detail
            psDetail = conn.prepareStatement(sqlDetail);
            for (OrderDetail detail : order.getDetails()) {
                 if(detail.getQuantity() <= 0) { 
                     continue;
                 }
                psDetail.setInt(1, orderId); //Dùng orderId vừa lấy được
                psDetail.setInt(2, detail.getProductId());
                psDetail.setInt(3, detail.getQuantity());
                psDetail.setDouble(4, detail.getSalePrice());
                psDetail.addBatch(); //Thêm vào lô để thực thi cùng lúc
            }
            
            //Thực thi lô lệnh INSERT detail
            int[] affectedRowsDetails = psDetail.executeBatch(); 

            //Kiểm tra xem có chi tiết nào bị lỗi không
            for (int result : affectedRowsDetails) {
                if (result == Statement.EXECUTE_FAILED) {
                    conn.rollback();
                    System.err.println("Lỗi: Không thể thêm một hoặc nhiều OrderDetail.");
                    return -1;
                }
            }
            
            if (affectedRowsDetails.length == 0 && order.getDetails().stream().anyMatch(d -> d.getQuantity() > 0)) {
                 conn.rollback();
                 System.err.println("Lỗi: Không thể thực thi batch cho OrderDetail.");
                 return -1;
            }


            //3.Cập nhật số lượng tồn kho sản phẩm trong bảng product
            psUpdateStock = conn.prepareStatement(sqlUpdateStock);
            boolean stockUpdateNeeded = false;
            for (OrderDetail detail : order.getDetails()) {
                 if(detail.getQuantity() <= 0) { 
                     continue;
                 }
                psUpdateStock.setInt(1, detail.getQuantity()); 
                psUpdateStock.setInt(2, detail.getProductId());
                psUpdateStock.setInt(3, detail.getQuantity()); 
                psUpdateStock.addBatch();
                stockUpdateNeeded = true;
            }
            if (stockUpdateNeeded) { 
                 int[] affectedRowsStock = psUpdateStock.executeBatch(); 
                 //Kiểm tra xem tất cả cập nhật tồn kho có thành công không             
                 for (int i = 0; i < affectedRowsStock.length; i++) {
                    // Tìm lại detail tương ứng (chỉ xét những detail có quantity > 0)
                     OrderDetail correspondingDetail = order.getDetails().stream()
                                                            .filter(d -> d.getQuantity() > 0)
                                                            .skip(i)
                                                            .findFirst()
                                                            .orElse(null);
                     int productIdToCheck = (correspondingDetail != null) ? correspondingDetail.getProductId() : -1;

                     if (affectedRowsStock[i] == 0) { // Nếu = 0 -> có thể do không đủ hàng
                         conn.rollback();
                         System.err.println("Lỗi: Cập nhật tồn kho thất bại cho sản phẩm ID " + productIdToCheck + ". Có thể do không đủ hàng.");
                         return -1;
                     }
                     if (affectedRowsStock[i] == Statement.EXECUTE_FAILED) {
                         conn.rollback();
                         System.err.println("Lỗi: Cập nhật tồn kho thất bại cho sản phẩm ID " + productIdToCheck + ".");
                         return -1;
                     }
                 }
            }


            // --- KẾT THÚC TRANSACTION ---
            conn.commit(); // Lưu tất cả thay đổi nếu mọi thứ thành công
            System.out.println("Thêm đơn hàng và cập nhật tồn kho thành công!");

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm đơn hàng: " + e.getMessage());
             e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.println("Transaction đang được rollback...");
                    conn.rollback(); // Hoàn tác tất cả nếu có lỗi xảy ra
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            //Đánh dấu thất bại
            orderId = -1;
        } finally {
            // Đóng các resource
             closeResource(generatedKeys);
             closeResource(psOrder);
             closeResource(psDetail);
             closeResource(psUpdateStock);
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return orderId; 
    }


     // --- Hàm tiện ích để đóng ResultSet ---
    private void closeResource(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // --- Hàm tiện ích để đóng PreparedStatement ---
    private void closeResource(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

