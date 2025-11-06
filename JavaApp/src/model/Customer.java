package model;

public class Customer {
    private int id;
    private String phone; 
    private String fullName; 

    public Customer() {
    }

    public Customer(int id, String phone, String fullName) {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
    }

    public Customer(String phone, String fullName) {
        this.phone = phone;
        this.fullName = fullName;
    }

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

