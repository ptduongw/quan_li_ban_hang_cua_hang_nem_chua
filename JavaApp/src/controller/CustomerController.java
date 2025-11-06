package controller;

import dao.CustomerDAO;
import model.Customer;
import java.util.List;

public class CustomerController {

    private CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
    
    public Customer findCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            System.err.println("Loi Controller: So dien thoai khong duoc de trong.");
            return null;
        }
        return customerDAO.getCustomerByPhone(phone.trim());
    }

    public int addCustomer(String phone, String fullName) {
        //Kiem tra du lieu dau vao
        if (phone == null || phone.trim().isEmpty() || fullName == null || fullName.trim().isEmpty()) {
            System.err.println("Loi Controller: So dien thoai va Ten khong duoc de trong.");
            return -1;
        }
        
        //Kiem tra SDT da ton tai chua
        if (customerDAO.getCustomerByPhone(phone.trim()) != null) {
             System.err.println("Loi Controller: So dien thoai " + phone + " da ton tai.");
             return -2;
        }

        Customer customer = new Customer(phone.trim(), fullName.trim());
        
        //Lưu khách hàng
        return customerDAO.addCustomer(customer);
    }

    public boolean updateCustomer(int id, String phone, String fullName) {
         if (id <= 0) {
             System.err.println("Loi Controller: ID khach hang khong hop le.");
             return false;
         }
         if (phone == null || phone.trim().isEmpty() || fullName == null || fullName.trim().isEmpty()) {
            System.err.println("Loi Controller: So dien thoai va Ten khong duoc de trong.");
            return false;
        }
        
        Customer customer = new Customer(id, phone.trim(), fullName.trim());
        return customerDAO.updateCustomer(customer);
    }
    
    public boolean deleteCustomer(int id) {
        if (id <= 0) {
             System.err.println("Loi Controller: ID khach hang khong hop le.");
             return false;
        }        
        return customerDAO.deleteCustomer(id);
    }
}
