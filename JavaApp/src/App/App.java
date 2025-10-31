package App;

import javax.swing.SwingUtilities;
import view.MainView; // Import lop MainView tu goi 'view'

/**
 * Lop nay la DIEM KHOI DAU (main entry point) cua toan bo ung dung.
 * Nhiem vu duy nhat cua no la tao va hien thi cua so chinh (MainView).
 */
public class App {

    public static void main(String[] args) {
        // Day la cach tieu chuan va an toan de khoi chay mot ung dung Swing
        // No dam bao rang giao dien duoc tao tren luong (thread) su kien cua Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Tao mot doi tuong MainView moi va cho no hien thi
                new MainView().setVisible(true);
            }
        });
    }
}

