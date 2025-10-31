package controller;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Order;
import model.OrderDetail;
import model.Product;

/**
 * Lớp này là "bộ não" logic cho Giao Diện Bán Hàng (SalesView).
 * Nó kết nối View với các lớp DAO tương ứng.
 */
public class SalesController {

    // Khai báo các "người vận chuyển" (DAO)
    private ProductDAO productDAO;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    /**
     * Hàm dựng (Constructor)
     * Khởi tạo các đối tượng DAO cần thiết.
     */
    public SalesController() {
        this.productDAO = new ProductDAO();
        this.customerDAO = new CustomerDAO();
        this.orderDAO = new OrderDAO();
    }

    // --- CÁC HÀM MÀ GIAO DIỆN (VIEW) SẼ GỌI ---

    /**
     * Được gọi bởi Cột 1 (View) để lấy danh sách sản phẩm hiển thị.
     * @return Danh sách tất cả sản phẩm.
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Được gọi bởi Cột 3 (View) khi nhấn nút "Tìm KH".
     * @param phone Số điện thoại khách hàng.
     * @return Đối tượng Customer nếu tìm thấy, ngược lại trả về null.
     */
    public Customer findCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        return customerDAO.getCustomerByPhone(phone.trim());
    }

    /**
     * Được gọi bởi Cột 3 (View) khi nhấn nút "THANH TOÁN".
     * Đây là logic nghiệp vụ quan trọng nhất.
     * * @param customer Đối tượng khách hàng (có thể là khách mới chưa có ID).
     * @param cartItems Danh sách các sản phẩm trong giỏ hàng (billTable).
     * @param totalAmount Tổng số tiền cuối cùng.
     * @return ID của đơn hàng mới nếu thành công, -1 nếu thất bại.
     */
    public int processPayment(Customer customer, List<OrderDetail> cartItems, double totalAmount) {
        
        // 1. Kiểm tra logic (validation)
        if (cartItems == null || cartItems.isEmpty()) {
            System.err.println("Lỗi Controller: Giỏ hàng trống!");
            return -1; // Trả về -1 (mã lỗi)
        }
        if (customer == null) {
            System.err.println("Lỗi Controller: Không có thông tin khách hàng!");
            return -1;
        }

        // 2. Xử lý khách hàng
        // Nếu khách hàng là mới (chưa có ID), thêm họ vào database trước.
        if (customer.getId() == 0) {
            System.out.println("Controller: Phát hiện khách hàng mới, đang thêm vào DB...");
            int newCustId = customerDAO.addCustomer(customer);
            if (newCustId == -1) {
                System.err.println("Lỗi Controller: Không thể thêm khách hàng mới!");
                return -1; // Thêm khách thất bại
            }
            customer.setId(newCustId); // Cập nhật ID cho đối tượng customer
            System.out.println("Controller: Đã thêm khách hàng mới với ID: " + newCustId);
        }

        // 3. Tạo đối tượng Order
        Order newOrder = new Order();
        newOrder.setCustomerId(customer.getId()); // Gán ID khách hàng (mới hoặc cũ)
        newOrder.setTotalAmount(totalAmount);
        newOrder.setDetails(cartItems); // Gán danh sách chi tiết (giỏ hàng)

        // 4. Gọi OrderDAO để xử lý (DAO sẽ lo phần transaction)
        System.out.println("Controller: Đang gửi đơn hàng đến DAO...");
        int newOrderId = orderDAO.addOrder(newOrder);

        if (newOrderId != -1) {
            System.out.println("Controller: Đã xử lý thanh toán thành công! ID Đơn hàng mới: " + newOrderId);
        } else {
            System.err.println("Lỗi Controller: OrderDAO báo lỗi khi thêm đơn hàng.");
        }

        return newOrderId; // Trả về ID đơn hàng mới (hoặc -1 nếu lỗi)
    }
}

