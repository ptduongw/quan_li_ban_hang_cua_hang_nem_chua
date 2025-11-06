package controller;

import dao.ProfitDAO;
import model.Profit;
import java.util.Date;
import java.util.List;

public class ProfitController {

    private ProfitDAO profitDAO;

    public ProfitController() {
        this.profitDAO = new ProfitDAO();
    }

    public List<Profit> getAllProfitRecords() {
        return profitDAO.getAllProfitRecords();
    }

    public boolean addProfitRecord(Date date, double revenue, double costMeat, double costSpices, double costLeaf, double costLabor) {
        
        //Kiem tra du lieu dau vao
        if (date == null) {
            System.err.println("Loi Controller: Ngay khong duoc de trong.");
            return false;
        }
        //Kiem tra cac so lieu khong duoc am
        if (revenue < 0 || costMeat < 0 || costSpices < 0 || costLeaf < 0 || costLabor < 0) {
             System.err.println("Loi Controller: Cac so tien (doanh thu, chi phi) khong duoc am.");
             return false;
        }
        
        Profit profit = new Profit(date, revenue, costMeat, costSpices, costLeaf, costLabor);

        //Lưu
        return profitDAO.addProfitRecord(profit);
    }
}
