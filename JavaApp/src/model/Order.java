// BẮT BUỘC: Dòng này phải là dòng đầu tiên
package model;

import java.util.Date;
import java.util.List;

/**
 * Lớp này đại diện cho một Hóa đơn (giống hệt cấu trúc bảng 'orders')
 * (File này chúng ta sẽ dùng sau, khi làm chức năng bán hàng)
 */
public class Order {
    private int id;
    private Date orderDate;
    private double totalAmount;
    private int customerId; // Liên kết với bảng customers

    // (Bạn có thể thêm các getters/setters/constructor ở đây sau)
    
}
