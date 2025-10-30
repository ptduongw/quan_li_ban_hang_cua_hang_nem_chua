package model;

import java.util.Date; // Sử dụng java.util.Date cho trường ngày tháng

/**
 * Lớp này đại diện cho một bản ghi lợi nhuận (bảng profit).
 */
public class Profit {
    private int id;
    private Date date; // Ngày ghi nhận
    private double revenue; // Doanh thu
    private double costMeat; // Chi phí thịt
    private double costSpices; // Chi phí gia vị
    private double costLeaf; // Chi phí lá
    private double costLabor; // Chi phí nhân công
    private double netProfit; // Lợi nhuận ròng

    // Constructor mặc định
    public Profit() {
    }

    // Constructor để tạo đối tượng từ dữ liệu DB (có ID)
    public Profit(int id, Date date, double revenue, double costMeat, double costSpices, double costLeaf, double costLabor, double netProfit) {
        this.id = id;
        this.date = date;
        this.revenue = revenue;
        this.costMeat = costMeat;
        this.costSpices = costSpices;
        this.costLeaf = costLeaf;
        this.costLabor = costLabor;
        this.netProfit = netProfit;
    }

    // Constructor để tạo đối tượng mới (chưa có ID, có thể tự tính lợi nhuận ròng)
    public Profit(Date date, double revenue, double costMeat, double costSpices, double costLeaf, double costLabor) {
        this.date = date;
        this.revenue = revenue;
        this.costMeat = costMeat;
        this.costSpices = costSpices;
        this.costLeaf = costLeaf;
        this.costLabor = costLabor;
        // Tự động tính lợi nhuận ròng
        this.calculateNetProfit(); 
    }

    // Hàm tiện ích để tự động tính lợi nhuận ròng
    public void calculateNetProfit() {
        double totalCost = this.costMeat + this.costSpices + this.costLeaf + this.costLabor;
        this.netProfit = this.revenue - totalCost;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getCostMeat() {
        return costMeat;
    }

    public void setCostMeat(double costMeat) {
        this.costMeat = costMeat;
    }

    public double getCostSpices() {
        return costSpices;
    }

    public void setCostSpices(double costSpices) {
        this.costSpices = costSpices;
    }

    public double getCostLeaf() {
        return costLeaf;
    }

    public void setCostLeaf(double costLeaf) {
        this.costLeaf = costLeaf;
    }

    public double getCostLabor() {
        return costLabor;
    }

    public void setCostLabor(double costLabor) {
        this.costLabor = costLabor;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

    @Override
    public String toString() {
        return "Profit{" +
               "id=" + id +
               ", date=" + date +
               ", revenue=" + revenue +
               ", costMeat=" + costMeat +
               ", costSpices=" + costSpices +
               ", costLeaf=" + costLeaf +
               ", costLabor=" + costLabor +
               ", netProfit=" + netProfit +
               '}';
    }
}

