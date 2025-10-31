package model;

/**
 * Lớp này đại diện cho một dòng chi tiết trong đơn hàng (bảng order_detail).
 */
public class OrderDetail {
    private int id;
    private int orderId;    // Khóa ngoại liên kết tới Order (order_table)
    private int productId;  // Khóa ngoại liên kết tới Product (product)
    private int quantity;   // Số lượng sản phẩm mua
    private double salePrice; // Giá bán tại thời điểm mua (có thể khác giá hiện tại)

    // Constructor mặc định (có thể cần cho một số thư viện hoặc framework)
    public OrderDetail() {
    }

    // === HÀM DỰNG MỚI ĐƯỢC THÊM VÀO ===
    /**
     * Constructor đầy đủ để tạo một chi tiết đơn hàng mới.
     * @param orderId ID của đơn hàng chứa chi tiết này.
     * @param productId ID của sản phẩm trong chi tiết này.
     * @param quantity Số lượng sản phẩm.
     * @param salePrice Giá bán của sản phẩm tại thời điểm mua.
     */
    public OrderDetail(int orderId, int productId, int quantity, double salePrice) {
        // ID sẽ tự tăng trong DB nên không cần set ở đây, nhưng vẫn cần tham số để khớp
        // Hoặc có thể tạo constructor không cần id ban đầu
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }


    // Constructor đầy đủ bao gồm cả ID (dùng khi đọc từ DB lên)
     public OrderDetail(int id, int orderId, int productId, int quantity, double salePrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
               "id=" + id +
               ", orderId=" + orderId +
               ", productId=" + productId +
               ", quantity=" + quantity +
               ", salePrice=" + salePrice +
               '}';
    }
}

