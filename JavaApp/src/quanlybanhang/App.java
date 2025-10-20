package quanlybanhang;

import javax.swing.SwingUtilities;
import view.MainView;

/**
 * Đây là lớp khởi động chính của toàn bộ ứng dụng.
 */
public class App {
    public static void main(String[] args) {
        // Sử dụng SwingUtilities.invokeLater để đảm bảo rằng
        // việc tạo và hiển thị giao diện người dùng được thực hiện
        // trên Event Dispatch Thread (EDT) của Swing.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Tạo một đối tượng của cửa sổ chính
                MainView mainView = new MainView();
                // Hiển thị cửa sổ
                mainView.setVisible(true);
            }
        });
    }
}
