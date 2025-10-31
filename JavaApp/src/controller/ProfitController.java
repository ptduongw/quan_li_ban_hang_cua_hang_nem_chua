package controller;

import dao.ProfitDAO;
import model.Profit;
import java.util.Date;
import java.util.List;

/**
 * Lop nay la "bo nao" logic cho Giao Dien Quan Ly Loi Nhuan (ProfitView).
 * No lam "bao ve" va la trung gian giua View va ProfitDAO.
 */
public class ProfitController {

    private ProfitDAO profitDAO;

    public ProfitController() {
        this.profitDAO = new ProfitDAO();
    }

    /**
     * Duoc goi boi View de lay danh sach tat ca ban ghi loi nhuan.
     * @return Danh sach Profit.
     */
    public List<Profit> getAllProfitRecords() {
        return profitDAO.getAllProfitRecords();
    }

    /**
     * Duoc goi boi View khi nhan nut "Them".
     * Kiem tra du lieu dau vao ("validation") truoc khi goi DAO.
     * @return true neu them thanh cong, false neu that bai.
     */
    public boolean addProfitRecord(Date date, double revenue, double costMeat, double costSpices, double costLeaf, double costLabor) {
        
        // --- BUOC 1: Kiem tra du lieu dau vao ---
        if (date == null) {
            System.err.println("Loi Controller: Ngay khong duoc de trong.");
            return false;
        }
        // Kiem tra cac so lieu khong duoc am
        if (revenue < 0 || costMeat < 0 || costSpices < 0 || costLeaf < 0 || costLabor < 0) {
             System.err.println("Loi Controller: Cac so tien (doanh thu, chi phi) khong duoc am.");
             return false;
        }
        
        // --- BUOC 2: Tao doi tuong Model ---
        // Loi nhuan rong (netProfit) se tu dong duoc tinh trong constructor cua Profit
        Profit profit = new Profit(date, revenue, costMeat, costSpices, costLeaf, costLabor);

        // --- BUOC 3: Goi DAO de luu ---
        return profitDAO.addProfitRecord(profit);
    }
}
