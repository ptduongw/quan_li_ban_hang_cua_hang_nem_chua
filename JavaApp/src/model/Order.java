package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private int customerId; 
    private Date orderDate; 
    private double totalAmount; 
    private List<OrderDetail> details; 

    public Order() {
        this.details = new ArrayList<>(); 
        this.orderDate = new Date(); 
        this.totalAmount = 0.0; 
    }

    public Order(int id, int customerId, Date orderDate, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.details = new ArrayList<>(); 
    }

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

    public void addDetail(OrderDetail detail) {
        this.details.add(detail);
    }

    @Override
    public String toString() {
        return "Order [ID=" + id + ", CustomerID=" + customerId + ", Ngay=" + orderDate +
               ", TongTien=" + String.format("%,.0f", totalAmount) + ", So chi tiet=" + (details != null ? details.size() : 0) + ']';
    }
}

