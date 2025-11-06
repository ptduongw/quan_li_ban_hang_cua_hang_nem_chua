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

public class SalesController {
    private ProductDAO productDAO;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    public SalesController() {
        this.productDAO = new ProductDAO();
        this.customerDAO = new CustomerDAO();
        this.orderDAO = new OrderDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }
 
    public Customer findCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        return customerDAO.getCustomerByPhone(phone.trim());
    }

    public int processPayment(Customer customer, List<OrderDetail> cartItems, double totalAmount) {    
        //Kiem tra logic(validation)
        if (cartItems == null || cartItems.isEmpty()) {
            System.err.println("Loi Controller: Gio hang trong!");
            return -1; 
        }
        if (customer == null) {
            System.err.println("Loi Controller: Khong co thong tin khach hang!");
            return -1;
        }

        //Xu ly khach hang
        if (customer.getId() == 0) {
            System.out.println("Controller: Phat hien khach hang moi, dang them vao DB...");
            int newCustId = customerDAO.addCustomer(customer);
            if (newCustId == -1) {
                System.err.println("Loi Controller: Khong the them khach hang moi!");
                return -1; 
            }
            customer.setId(newCustId); 
            System.out.println("Controller: Da them khach hang moi voi ID: " + newCustId);
        }

        //Tao doi tuong Order
        Order newOrder = new Order();
        newOrder.setCustomerId(customer.getId()); 
        newOrder.setTotalAmount(totalAmount);
        newOrder.setDetails(cartItems); //Gan danh sach chi tiet(gio hang)

        //Goi OrderDAO de xu ly transaction
        System.out.println("Controller: Dang gui don hang den DAO...");
        int newOrderId = orderDAO.addOrder(newOrder);

        if (newOrderId != -1) {
            System.out.println("Controller: Da xu ly thanh toan thanh cong! ID Don hang moi: " + newOrderId);
        } else {
            System.err.println("Loi Controller: OrderDAO bao loi khi them don hang.");
        }
        return newOrderId; 
    }
}

