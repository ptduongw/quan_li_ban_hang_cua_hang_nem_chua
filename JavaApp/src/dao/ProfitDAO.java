package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date; // Dùng java.util.Date
import java.util.List;
import model.Profit;
import utils.DatabaseConnector;

/**
 * Lớp này xử lý các thao tác CRUD liên quan đến bảng 'profit'.
 */
public class ProfitDAO {

    /**
     * Thêm một bản ghi lợi nhuận mới vào database.
     * @param profit Đối tượng Profit chứa thông tin cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addProfitRecord(Profit profit) {
        String sql = "INSERT INTO profit (date, revenue, cost_meat, cost_spices, cost_leaf, cost_labor, net_profit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);

            // Chuyển đổi java.util.Date sang java.sql.Date
            pstmt.setDate(1, new java.sql.Date(profit.getDate().getTime()));
            pstmt.setDouble(2, profit.getRevenue());
            pstmt.setDouble(3, profit.getCostMeat());
            pstmt.setDouble(4, profit.getCostSpices());
            pstmt.setDouble(5, profit.getCostLeaf());
            pstmt.setDouble(6, profit.getCostLabor());
            pstmt.setDouble(7, profit.getNetProfit());

            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm bản ghi lợi nhuận: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        return success;
    }

    /**
     * Lấy tất cả các bản ghi lợi nhuận từ database.
     * @return Danh sách các đối tượng Profit.
     */
    public List<Profit> getAllProfitRecords() {
        List<Profit> profitList = new ArrayList<>();
        String sql = "SELECT * FROM profit ORDER BY date DESC"; // Sắp xếp theo ngày mới nhất trước
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return profitList;
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Profit profit = new Profit(
                        rs.getInt("id"),
                        rs.getDate("date"), // Lấy về kiểu java.sql.Date, tương thích với java.util.Date
                        rs.getDouble("revenue"),
                        rs.getDouble("cost_meat"),
                        rs.getDouble("cost_spices"),
                        rs.getDouble("cost_leaf"),
                        rs.getDouble("cost_labor"),
                        rs.getDouble("net_profit")
                );
                profitList.add(profit);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách lợi nhuận: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return profitList;
    }

    // --- Các hàm khác có thể thêm sau (ví dụ: lấy theo ngày, cập nhật, xóa) ---

    // --- Hàm tiện ích để đóng tài nguyên ---
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { /* Bỏ qua */ }
        try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* Bỏ qua */ }
        // Không đóng Connection ở đây
    }
}

