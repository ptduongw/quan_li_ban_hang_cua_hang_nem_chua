package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Customer; 
import utils.DatabaseConnector;

//Bảng customer
public class CustomerDAO {
    //Lấy dữ liệu khách hàng từ sql
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        Connection conn = null;
        PreparedStatement pstmt = null;//Chuẩn bị và thực thi câu lệnh SQL
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return customerList;
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("phone"), 
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

    //Tìm khách hàng theo ID
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

    //Tìm khách hàng theo số điện thoại
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

    //Thêm khách hàng mới
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
            // Kiểm tra lỗi trùng số điện thoại 
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

    //Cập nhật thông tin khách hàng
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

    //Xóa khách hàng theo ID
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

    //Kiểm tra xem một khách hàng có đơn hàng nào liên kết theo ID.
    private boolean hasOrders(int customerId) {
        String sql = "SELECT 1 FROM order_table WHERE customer_id = ? LIMIT 1"; // Chỉ cần tìm 1 là đủ
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean hasOrder = false;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return true; //Coi như có đơn hàng
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();
            hasOrder = rs.next(); //rs.next() == true thì có đơn hàng
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra đơn hàng của khách hàng: " + e.getMessage());
            e.printStackTrace();
            return true; //Coi như có đơn hàng
        } finally {
             closeResource(pstmt);
             closeResource(rs);
        }
        return hasOrder;
    }


    //Đóng tài nguyên
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        closeResource(rs);
        closeResource(pstmt);
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

