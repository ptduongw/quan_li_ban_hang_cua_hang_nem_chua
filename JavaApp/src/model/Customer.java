package model;

/**
 * Lớp này đại diện cho một khách hàng (bảng customer).
 */
public class Customer {
    private int id;
    private String phone; // Tên cột trong DB của bạn là 'phone'
    private String fullName; // Tên cột trong DB của bạn là 'full_name'

    // Constructor mặc định
    public Customer() {
    }

    // Constructor để tạo đối tượng từ dữ liệu DB (có ID)
    public Customer(int id, String phone, String fullName) {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
    }

    // Constructor để tạo đối tượng mới (chưa có ID)
    public Customer(String phone, String fullName) {
        this.phone = phone;
        this.fullName = fullName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Customer{" +
               "id=" + id +
               ", phone='" + phone + '\'' +
               ", fullName='" + fullName + '\'' +
               '}';
    }
}

