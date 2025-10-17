package Service;

import model.Order;
import model.Product;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Product product, int quantity) {
        if (product.getQuantity() >= quantity) {
            orders.add(new Order(product, quantity));
            product.setQuantity(product.getQuantity() - quantity);
            System.out.println("Đã ghi nhận đơn hàng!");
        } else {
            System.out.println("Không đủ hàng trong kho!");
        }
    }

    public void showOrders() {
        System.out.println("===== DANH SÁCH ĐƠN HÀNG =====");
        double total = 0;
        for (Order o : orders) {
            System.out.println(o);
            total += o.getTotal();
        }
        System.out.println("Tổng doanh thu: " + total + "đ");
    }
}
