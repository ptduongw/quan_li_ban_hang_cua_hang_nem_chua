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
 * Lop nay la "bo nao" logic cho Giao Dien Ban Hang (SalesView).
 * No ket noi View voi cac lop DAO tuong ung.
 */
public class SalesController {

    // Khai bao cac "nguoi van chuyen" (DAO)
    private ProductDAO productDAO;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    /**
     * Ham dung (Constructor)
     * Khoi tao cac doi tuong DAO can thiet.
     */
    public SalesController() {
        this.productDAO = new ProductDAO();
        this.customerDAO = new CustomerDAO();
        this.orderDAO = new OrderDAO();
    }

    // --- CAC HAM MA GIAO DIEN (VIEW) SE GOI ---

    /**
     * Duoc goi boi Cot 1 (View) de lay danh sach san pham hien thi.
     * @return Danh sach tat ca san pham.
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    // --- HAM MOI DUOC THEM VAO (DE SUA LOI BUILD) ---
    /**
     * Duoc goi boi View khi click dup vao bang san pham de lay chi tiet.
     * @param id ID cua san pham
     * @return Doi tuong Product
     */
    public Product getProductById(int id) {
        // Chi can goi qua ProductDAO
        return productDAO.getProductById(id);
    }
    // -------------------------------------------------

    /**
     * Duoc goi boi Cot 3 (View) khi nhan nut "Tim KH".
     * @param phone So dien thoai khach hang.
     * @return Doi tuong Customer neu tim thay, nguoc lai tra ve null.
     */
    public Customer findCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        return customerDAO.getCustomerByPhone(phone.trim());
    }

    /**
     * Duoc goi boi Cot 3 (View) khi nhan nut "THANH TOAN".
     * Day la logic nghiep vu quan trong nhat.
     * @param customer Doi tuong khach hang (co the la khach moi chua co ID).
     * @param cartItems Danh sach cac san pham trong gio hang (billTable).
     * @param totalAmount Tong so tien cuoi cung.
     * @return ID cua don hang moi neu thanh cong, -1 neu that bai.
     */
    public int processPayment(Customer customer, List<OrderDetail> cartItems, double totalAmount) {
        
        // 1. Kiem tra logic (validation)
        if (cartItems == null || cartItems.isEmpty()) {
            System.err.println("Loi Controller: Gio hang trong!");
            return -1; // Tra ve -1 (ma loi)
        }
        if (customer == null) {
            System.err.println("Loi Controller: Khong co thong tin khach hang!");
            return -1;
        }

        // 2. Xu ly khach hang
        // Neu khach hang la moi (chua co ID), them ho vao database truoc.
        if (customer.getId() == 0) {
            System.out.println("Controller: Phat hien khach hang moi, dang them vao DB...");
            int newCustId = customerDAO.addCustomer(customer);
            if (newCustId == -1) {
                System.err.println("Loi Controller: Khong the them khach hang moi!");
                return -1; // Them khach that bai
            }
            customer.setId(newCustId); // Cap nhat ID cho doi tuong customer
            System.out.println("Controller: Da them khach hang moi voi ID: " + newCustId);
        }

        // 3. Tao doi tuong Order
        Order newOrder = new Order();
        newOrder.setCustomerId(customer.getId()); // Gan ID khach hang (moi hoac cu)
        newOrder.setTotalAmount(totalAmount);
        newOrder.setDetails(cartItems); // Gan danh sach chi tiet (gio hang)

        // 4. Goi OrderDAO de xu ly (DAO se lo phan transaction)
        System.out.println("Controller: Dang gui don hang den DAO...");
        int newOrderId = orderDAO.addOrder(newOrder);

        if (newOrderId != -1) {
            System.out.println("Controller: Da xu ly thanh toan thanh cong! ID Don hang moi: " + newOrderId);
        } else {
            System.err.println("Loi Controller: OrderDAO bao loi khi them don hang.");
        }

        return newOrderId; // Tra ve ID don hang moi (hoac -1 neu loi)
    }
}

