// BẮT BUỘC: Dòng này phải là dòng đầu tiên, khớp với tên package
package App;

// Import lớp MainView từ gói view
import view.MainView;

/**
 * Lớp này là điểm khởi đầu (main entry point) của toàn bộ ứng dụng.
 * Nhiệm vụ duy nhất của nó là tạo và hiển thị cửa sổ MainView.
 */
public class App {
    
    public static void main(String[] args) {
        // Đây là cách tiêu chuẩn và an toàn để khởi chạy một ứng dụng Swing
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Tạo một đối tượng MainView và cho nó hiển thị
                new MainView().setVisible(true);
            }
        });
    }
}
