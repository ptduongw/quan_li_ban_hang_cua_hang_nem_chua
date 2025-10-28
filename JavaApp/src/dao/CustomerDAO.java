package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Customer; // Import lớp Customer
import utils.DatabaseConnector;

/**
 * Lớp này xử lý các thao tác CRUD cho bảng 'customer'.
 */
public class CustomerDAO {

    /**
     * Lấy tất cả khách hàng từ database.
     * @return Danh sách các đối tượng Customer.
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return customerList;
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("phone"), // Đã sửa tên cột phone_number -> phone
                        rs.getString("full_name")
                );
                customerList.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return customerList;
    }

    /**
     * Tìm khách hàng theo ID.
     * @param id ID của khách hàng.
     * @return Đối tượng Customer nếu tìm thấy, null nếu không.
     */
    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return null;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("phone"),
                        rs.getString("full_name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khách hàng theo ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return customer;
    }

     /**
     * Tìm khách hàng theo số điện thoại.
     * @param phone Số điện thoại của khách hàng.
     * @return Đối tượng Customer nếu tìm thấy, null nếu không.
     */
    public Customer getCustomerByPhone(String phone) {
        String sql = "SELECT * FROM customer WHERE phone = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return null;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("phone"),
                        rs.getString("full_name")
                );
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khách hàng theo số điện thoại: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return customer;
    }

    /**
     * Thêm một khách hàng mới.
     * @param customer Đối tượng Customer chứa thông tin.
     * @return ID của khách hàng mới, -1 nếu thất bại.
     */
    public int addCustomer(Customer customer) {
        String sql = "INSERT INTO customer (phone, full_name) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int generatedId = -1;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return -1;
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, customer.getPhone());
            pstmt.setString(2, customer.getFullName());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Kiểm tra lỗi trùng số điện thoại (UNIQUE constraint)
             if (e.getErrorCode() == 1062) { // Mã lỗi cho UNIQUE constraint violation
                 System.err.println("Lỗi khi thêm khách hàng: Số điện thoại '" + customer.getPhone() + "' đã tồn tại.");
             } else {
                System.err.println("Lỗi SQL khi thêm khách hàng: " + e.getMessage());
                e.printStackTrace();
             }
        } finally {
            closeResources(conn, pstmt, generatedKeys);
        }
        return generatedId;
    }

    /**
     * Cập nhật thông tin khách hàng.
     * @param customer Đối tượng Customer chứa thông tin cập nhật (phải có ID).
     * @return true nếu thành công, false nếu thất bại.
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET phone = ?, full_name = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, customer.getPhone());
            pstmt.setString(2, customer.getFullName());
            pstmt.setInt(3, customer.getId());

            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
             if (e.getErrorCode() == 1062) {
                 System.err.println("Lỗi khi cập nhật khách hàng: Số điện thoại '" + customer.getPhone() + "' đã tồn tại cho khách hàng khác.");
             } else {
                System.err.println("Lỗi SQL khi cập nhật khách hàng: " + e.getMessage());
                e.printStackTrace();
             }
        } finally {
            closeResources(conn, pstmt, null);
        }
        return success;
    }

    /**
     * Xóa một khách hàng theo ID.
     * Lưu ý: Cần xử lý ràng buộc khóa ngoại nếu có đơn hàng liên kết.
     * Cách đơn giản là không cho xóa nếu có đơn hàng.
     * @param id ID của khách hàng cần xóa.
     * @return true nếu thành công, false nếu thất bại (ví dụ: do có đơn hàng liên kết).
     */
    public boolean deleteCustomer(int id) {
        // Trước tiên, kiểm tra xem khách hàng có đơn hàng nào không
        if (hasOrders(id)) {
            System.err.println("Không thể xóa khách hàng ID " + id + " vì có đơn hàng liên kết.");
            return false;
        }

        String sql = "DELETE FROM customer WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khách hàng: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        return success;
    }

    /**
     * Kiểm tra xem một khách hàng có đơn hàng nào liên kết không.
     * @param customerId ID của khách hàng.
     * @return true nếu có ít nhất một đơn hàng, false nếu không.
     */
    private boolean hasOrders(int customerId) {
        String sql = "SELECT 1 FROM order_table WHERE customer_id = ? LIMIT 1"; // Chỉ cần tìm 1 là đủ
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean hasOrder = false;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return true; // An toàn -> coi như có
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();
            hasOrder = rs.next(); // Nếu rs.next() là true -> có kết quả -> có đơn hàng
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra đơn hàng của khách hàng: " + e.getMessage());
            e.printStackTrace();
             return true; // An toàn -> coi như có
        } finally {
            // Chỉ đóng pstmt và rs, conn sẽ được đóng bởi hàm gọi deleteCustomer
             closeResource(pstmt);
             closeResource(rs);
        }
        return hasOrder;
    }


    // --- Hàm tiện ích để đóng tài nguyên ---
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        closeResource(rs);
        closeResource(pstmt);
        // Không đóng Connection ở đây
    }
     private void closeResource(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    private void closeResource(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

