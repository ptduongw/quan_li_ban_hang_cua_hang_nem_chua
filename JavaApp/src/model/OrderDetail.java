package model;

public class OrderDetail {
    private int id;
    private int orderId;   
    private int productId; 
    private int quantity;   //Số lượng
    private double salePrice;

    public OrderDetail() {
    }

    public OrderDetail(int orderId, int productId, int quantity, double salePrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

     public OrderDetail(int id, int orderId, int productId, int quantity, double salePrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

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

