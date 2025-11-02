package App;

import javax.swing.SwingUtilities;
import view.ProductManagementView; // <<< IMPORT FILE MOI
// import view.SalesView; // (Tam thoi khong dung file nay)

/**
 * Lop nay la DIEM KHOI DAU (main entry point) cua toan bo ung dung.
 * (File nay khong dau de tranh loi font)
 */
public class App {

    public static void main(String[] args) {
        // Day la cach tieu chuan va an toan de khoi chay mot ung dung Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Tam thoi an cua so Ban Hang (SalesView)
                // new SalesView().setVisible(true);
                
                // <<< CHAY CUA SO QUAN LY SAN PHAM DE TEST >>>
                new ProductManagementView().setVisible(true);
            }
        });
    }
}

