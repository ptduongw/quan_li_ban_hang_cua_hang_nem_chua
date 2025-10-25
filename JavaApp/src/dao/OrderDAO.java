// BẮT BUỘC: Dòng này phải là dòng đầu tiên
package dao;

// Import các lớp cần thiết
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// (Sau này chúng ta sẽ import model.Order, model.OrderDetail...)
// import model.Order;
// import model.OrderDetail;
import utils.DatabaseConnector;

/**
 * Lớp này chịu trách nhiệm truy vấn cơ sở dữ liệu cho bảng 'orders' và 'order_details'.
 * (Hiện tại chỉ cần khai báo lớp để hết lỗi build)
 */
public class OrderDAO {
    
    public OrderDAO() {
        // Hàm khởi tạo
    }

    // (Sau này chúng ta sẽ viết hàm addOrder phức tạp ở đây)
    // Nó sẽ cần phải:
    // 1. Thêm 1 dòng vào bảng 'orders'
    // 2. Lấy ID của order vừa tạo
    // 3. Thêm nhiều dòng vào bảng 'order_details' với ID đó
    // 4. Cập nhật lại số lượng 'stock' trong bảng 'products'
    
    // public boolean addOrder(Order order, List<OrderDetail> details) { ... } 
}

