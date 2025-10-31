package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Lớp này đại diện cho một Đơn Hàng / Hóa Đơn (ánh xạ tới bảng 'order_table').
 */
public class Order {
    private int id;
    private int customerId; // ID của khách hàng (tham chiếu tới bảng customer)
    private Date orderDate; // Ngày tạo đơn hàng
    private double totalAmount; // Tổng tiền của đơn hàng
    private List<OrderDetail> details; // Danh sách các chi tiết (sản phẩm) trong đơn hàng

    // Hàm dựng mặc định
    public Order() {
        this.details = new ArrayList<>(); // Khởi tạo danh sách chi tiết
        this.orderDate = new Date(); // Mặc định ngày tạo là hiện tại
        this.totalAmount = 0.0; // Mặc định tổng tiền là 0
    }

    // Hàm dựng đầy đủ (thường dùng khi đọc từ database)
    public Order(int id, int customerId, Date orderDate, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.details = new ArrayList<>(); // Khởi tạo danh sách chi tiết
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    // Phương thức tiện ích để thêm chi tiết vào đơn hàng
    public void addDetail(OrderDetail detail) {
        this.details.add(detail);
    }

    @Override
    public String toString() {
        return "Order [ID=" + id + ", CustomerID=" + customerId + ", Ngay=" + orderDate +
               ", TongTien=" + String.format("%,.0f", totalAmount) + ", So chi tiet=" + (details != null ? details.size() : 0) + ']';
    }
}

