package App;

import javax.swing.SwingUtilities;
// Chung ta se cho no chay thang SalesView de test giao dien
import view.SalesView; 
// import view.MainView; // Chung ta se dung file nay sau

/**
 * Lop nay la DIEM KHOI DAU (main entry point) cua toan bo ung dung.
 * Nhiem vu duy nhat cua no la tao va hien thi cua so chinh.
 */
public class App {

    public static void main(String[] args) {
        // Day la cach tieu chuan va an toan de khoi chay mot ung dung Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Hien tai, chung ta cho no chay thang SalesView de lam viec
                new SalesView().setVisible(true);
                
                // Sau nay, khi ban lam xong MainView, ban se doi lai thanh:
                // new MainView().setVisible(true);
            }
        });
    }
}

