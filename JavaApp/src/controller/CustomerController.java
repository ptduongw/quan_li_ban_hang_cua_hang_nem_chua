package controller;

import dao.CustomerDAO;
import model.Customer;
import java.util.List;

/**
 * Lop nay la "bo nao" logic cho Giao Dien Quan Ly Khach Hang (CustomerManagementView).
 * No lam "bao ve" va la trung gian giua View va CustomerDAO.
 */
public class CustomerController {

    private CustomerDAO customerDAO;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Duoc goi boi View de lay danh sach tat ca khach hang.
     * @return Danh sach Customer.
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Duoc goi boi View de tim khach hang bang SĐT.
     * @param phone So dien thoai.
     * @return Customer neu tim thay, nguoc lai null.
     */
    public Customer findCustomerByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            System.err.println("Loi Controller: So dien thoai khong duoc de trong.");
            return null;
        }
        return customerDAO.getCustomerByPhone(phone.trim());
    }

    /**
     * Duoc goi boi View khi nhan nut "Them".
     * Kiem tra du lieu dau vao truoc khi goi DAO.
     * @param phone So dien thoai.
     * @param fullName Ho va ten.
     * @return ID cua khach hang moi (lon hon 0) neu thanh cong, 
     * -1 neu that bai (validation fail), 
     * -2 neu SDT da ton tai.
     */
    public int addCustomer(String phone, String fullName) {
        // --- BUOC 1: Kiem tra du lieu dau vao ---
        if (phone == null || phone.trim().isEmpty() || fullName == null || fullName.trim().isEmpty()) {
            System.err.println("Loi Controller: So dien thoai va Ten khong duoc de trong.");
            return -1; // Ma loi: Du lieu khong hop le
        }
        
        // --- BUOC 2: Kiem tra logic nghiep vu (vi du: SDT da ton tai chua) ---
        // (Trong DB, cot 'phone' la UNIQUE)
        if (customerDAO.getCustomerByPhone(phone.trim()) != null) {
             System.err.println("Loi Controller: So dien thoai " + phone + " da ton tai.");
             return -2; // Ma loi: Trung SDT
        }
        
        // --- BUOC 3: Tao doi tuong Model ---
        Customer customer = new Customer(phone.trim(), fullName.trim());
        
        // --- BUOC 4: Goi DAO de luu ---
        return customerDAO.addCustomer(customer);
    }
    
    /**
     * Duoc goi boi View khi nhan nut "Sua".
     * Kiem tra du lieu truoc khi goi DAO.
     * @return true neu cap nhat thanh cong, false neu that bai.
     */
    public boolean updateCustomer(int id, String phone, String fullName) {
         if (id <= 0) {
             System.err.println("Loi Controller: ID khach hang khong hop le.");
             return false;
         }
         if (phone == null || phone.trim().isEmpty() || fullName == null || fullName.trim().isEmpty()) {
            System.err.println("Loi Controller: So dien thoai va Ten khong duoc de trong.");
            return false;
        }
        
        // (Co the them logic kiem tra SĐT trung voi nguoi khac o day neu can)
         
        Customer customer = new Customer(id, phone.trim(), fullName.trim());
        return customerDAO.updateCustomer(customer);
    }
    
    /**
     * Duoc goi boi View khi nhan nut "Xoa".
     * Kiem tra ID truoc khi goi DAO.
     * @param id ID khach hang can xoa.
     * @return true neu xoa thanh cong, false neu that bai.
     */
    public boolean deleteCustomer(int id) {
        if (id <= 0) {
             System.err.println("Loi Controller: ID khach hang khong hop le.");
             return false;
        }
        
        // Luu y: CustomerDAO.deleteCustomer se tu dong that bai (tra ve false)
        // neu khach hang do dang co don hang (do rang buoc khoa ngoai),
        // dieu nay la an toan.
        
        return customerDAO.deleteCustomer(id);
    }
}
