package Test;

import dao.CustomerDAO; // Thêm import này
import dao.OrderDAO;
import dao.ProductDAO;
import java.sql.Connection;
import java.util.List;
import model.Customer; // Thêm import này
import model.Order;
import model.OrderDetail;
import model.Product;
import utils.DatabaseConnector;
import java.util.ArrayList;
import java.sql.SQLException;

/**
 * Lớp này dùng để kiểm tra kết nối tới database và các hàm CRUD cơ bản.
 * Nó không phải là phần chính của ứng dụng, chỉ dùng để test.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("--- BƯỚC 1: KIỂM TRA KẾT NỐI DATABASE ---");
        Connection conn = DatabaseConnector.getConnection();

        if (conn != null) {
            System.out.println("--------------------------------------------------");
            System.out.println("✅ KET NOI THANH CONG!");
            System.out.println("   Ban da san sang de lam viec voi database '" + getDatabaseName(conn) + "'.");
            System.out.println("--------------------------------------------------");

            // --- BƯỚC 2: KIỂM TRA ProductDAO.getAllProducts() ---
            testGetAllProducts();

            // --- BƯỚC 3, 4, 5: KIỂM TRA ProductDAO CRUD ---
            testProductCRUD();

            // --- BƯỚC 6: KIỂM TRA OrderDAO.addOrder() ---
            testAddOrder();

            // --- BƯỚC 7: KIỂM TRA CustomerDAO CRUD ---
            testCustomerCRUD(); // <<< GỌI HÀM TEST MỚI

            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("--------------------------------------------------");
            System.err.println("❌ KET NOI THAT BAI!");
            System.err.println("   Vui long kiem tra lai DB_URL, USER, va PASS trong file DatabaseConnector.java");
            System.out.println("--------------------------------------------------");
        }
    }

    // Hàm lấy tên database từ connection (để hiển thị)
    private static String getDatabaseName(Connection conn) {
        try {
            return conn.getCatalog();
        } catch (SQLException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    // --- CÁC HÀM TEST ---

    public static void testGetAllProducts() {
        System.out.println("\n--- BƯỚC 2: TEST READ (getAllProducts) ---");
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.getAllProducts();

        if (productList.isEmpty()) {
            System.err.println("   Lỗi: Không tìm thấy sản phẩm nào, hoặc có lỗi khi lấy dữ liệu.");
        } else {
            System.out.println("   Tìm thấy " + productList.size() + " san pham:");
            for (Product p : productList) {
                System.out.println("      " + p.toString()); // In thông tin sản phẩm
            }
        }
        System.out.println("--- KẾT THÚC BƯỚC 2 ---");
    }

    public static void testProductCRUD() {
        ProductDAO productDAO = new ProductDAO();
        int newProductId = -1; // Biến để lưu ID sản phẩm mới

        // --- BƯỚC 3: TEST CREATE (addProduct) ---
        System.out.println("\n--- BƯỚC 3: TEST CREATE (addProduct) ---");
        Product newProduct = new Product("Bánh Mì Test", "piece", 15000.0, 50, "Ghi chu test");
        int generatedId = productDAO.addProduct(newProduct);
        if (generatedId != -1) {
            newProductId = generatedId; // Lưu lại ID
            System.out.println("   ✅ Them thanh cong san pham moi! ID moi la: " + newProductId);
            Product addedProduct = productDAO.getProductById(newProductId);
            if (addedProduct != null) {
                 System.out.println("      -> " + addedProduct.toString());
            } else {
                 System.err.println("      Lỗi: Không thể lấy lại sản phẩm vừa thêm.");
            }
        } else {
            System.err.println("   ❌ Them san pham moi THAT BAI!");
        }
        System.out.println("--- KẾT THÚC BƯỚC 3 ---");

        if (newProductId != -1) {
            // --- BƯỚC 4: TEST UPDATE (updateProduct) ---
            System.out.println("\n--- BƯỚC 4: TEST UPDATE (updateProduct) ---");
            Product productToUpdate = productDAO.getProductById(newProductId);
            if (productToUpdate != null) {
                 productToUpdate.setPrice(20000.0);
                 productToUpdate.setQuantity(40);
                 boolean updateSuccess = productDAO.updateProduct(productToUpdate);
                 if (updateSuccess) {
                     System.out.println("   ✅ Cap nhat thanh cong san pham ID: " + newProductId);
                     Product updatedProduct = productDAO.getProductById(newProductId);
                     System.out.println("      -> " + updatedProduct.toString());
                 } else {
                     System.err.println("   ❌ Cap nhat san pham THAT BAI!");
                 }
            } else {
                 System.err.println("   Lỗi: Không tìm thấy sản phẩm ID " + newProductId + " để cập nhật.");
            }
            System.out.println("--- KẾT THÚC BƯỚC 4 ---");

            // --- BƯỚC 5: TEST DELETE (deleteProduct) ---
            System.out.println("\n--- BƯỚC 5: TEST DELETE (deleteProduct) ---");
            boolean deleteSuccess = productDAO.deleteProduct(newProductId);
            if (deleteSuccess) {
                System.out.println("   ✅ Xoa thanh cong san pham ID: " + newProductId);
                Product deletedProduct = productDAO.getProductById(newProductId);
                if (deletedProduct == null) {
                    System.out.println("      -> San pham ID " + newProductId + " da bi xoa khoi DB.");
                } else {
                    System.err.println("      Lỗi: Van tim thay san pham sau khi xoa!");
                }
            } else {
                System.err.println("   ❌ Xoa san pham THAT BAI!");
            }
            System.out.println("--- KẾT THÚC BƯỚC 5 ---");
        } else {
             System.out.println("\n--- BỎ QUA BƯỚC 4 & 5 do BƯỚC 3 thất bại ---");
        }
    }

    public static void testAddOrder() {
        System.out.println("\n--- BƯỚC 6: TEST CREATE ORDER (addOrder) ---");
        OrderDAO orderDAO = new OrderDAO();
        ProductDAO productDAO = new ProductDAO();
        CustomerDAO customerDAO = new CustomerDAO(); // Cần để lấy khách hàng

        // Lấy khách hàng có ID = 1 (hoặc tạo mới nếu chưa có)
        Customer customer = customerDAO.getCustomerById(1);
        if (customer == null) {
             System.out.println("   Khách hàng ID 1 không tồn tại, đang thử tạo mới...");
             customer = new Customer("0912345678", "Nguyen Van A");
             int newCustId = customerDAO.addCustomer(customer);
             if (newCustId != -1) {
                 customer.setId(newCustId); // Gán ID mới
                 System.out.println("   -> Đã tạo khách hàng mới: " + customer.toString());
             } else {
                 System.err.println("   Lỗi: Không thể tạo khách hàng ID 1 để test.");
                 System.out.println("--- KẾT THÚC BƯỚC 6 (Thất bại sớm) ---");
                 return;
             }
        } else {
             System.out.println("   Sử dụng khách hàng có sẵn: " + customer.toString());
        }


        Order newOrder = new Order();
        newOrder.setCustomerId(customer.getId()); // Gán ID khách hàng

        List<OrderDetail> details = new ArrayList<>();

        Product product1 = productDAO.getProductById(1);
        if (product1 != null && product1.getQuantity() >= 1) {
             OrderDetail detail1 = new OrderDetail(0, 1, 1, product1.getPrice());
             details.add(detail1);
        } else {
             System.err.println("   Lỗi: Không tìm thấy sản phẩm ID 1 hoặc không đủ hàng (cần 1).");
             System.out.println("--- KẾT THÚC BƯỚC 6 (Thất bại sớm) ---");
             return;
        }

        Product product2 = productDAO.getProductById(2);
        if (product2 != null && product2.getQuantity() >= 2) {
            OrderDetail detail2 = new OrderDetail(0, 2, 2, product2.getPrice());
            details.add(detail2);
        } else {
             System.err.println("   Lỗi: Không tìm thấy sản phẩm ID 2 hoặc không đủ hàng (cần 2).");
             System.out.println("--- KẾT THÚC BƯỚC 6 (Thất bại sớm) ---");
             return;
        }

        newOrder.setDetails(details);

        double total = 0;
        for (OrderDetail d : newOrder.getDetails()) {
            total += d.getQuantity() * d.getSalePrice();
        }
        newOrder.setTotalAmount(total);

        System.out.println("   Đang thử thêm đơn hàng cho KH ID " + customer.getId() + " với tổng tiền: " + String.format("%,.0f", total));

        int generatedOrderId = orderDAO.addOrder(newOrder);

        if (generatedOrderId != -1) {
            System.out.println("   ✅ Them thanh cong don hang moi! ID don hang la: " + generatedOrderId);
            System.out.println("      -> KIỂM TRA TRONG MYSQL WORKBENCH:");
            System.out.println("         - Bảng 'order_table' phải có đơn hàng ID " + generatedOrderId);
            System.out.println("         - Bảng 'order_detail' phải có 2 dòng ứng với order_id " + generatedOrderId);
            System.out.println("         - Số lượng tồn kho (quantity) của sản phẩm ID 1 và 2 trong bảng 'product' phải bị giảm đi.");
        } else {
            System.err.println("   ❌ Them don hang moi THAT BAI!");
        }
        System.out.println("--- KẾT THÚC BƯỚC 6 ---");
    }

    // --- HÀM TEST MỚI CHO CUSTOMER ---
    public static void testCustomerCRUD() {
        System.out.println("\n--- BƯỚC 7: TEST CUSTOMER CRUD ---");
        CustomerDAO customerDAO = new CustomerDAO();
        int newCustomerId = -1; // Biến để lưu ID khách hàng mới

        // 7.1: Lấy danh sách khách hàng hiện có
        System.out.println("   7.1: Lấy danh sách khách hàng hiện có...");
        List<Customer> currentCustomers = customerDAO.getAllCustomers();
        if (currentCustomers.isEmpty()) {
            System.out.println("      -> Chưa có khách hàng nào.");
        } else {
            System.out.println("      -> Tìm thấy " + currentCustomers.size() + " khách hàng:");
            for (Customer c : currentCustomers) {
                System.out.println("         " + c.toString());
            }
        }

        // 7.2: Thêm khách hàng mới
        System.out.println("\n   7.2: Thêm khách hàng mới...");
        Customer newCustomer = new Customer("0333444555", "Khach Hang Test");
        int generatedId = customerDAO.addCustomer(newCustomer);
        if (generatedId != -1) {
            newCustomerId = generatedId;
            System.out.println("      ✅ Them thanh cong khach hang moi! ID moi la: " + newCustomerId);
            Customer addedCustomer = customerDAO.getCustomerById(newCustomerId);
            if (addedCustomer != null) {
                System.out.println("         -> " + addedCustomer.toString());
            } else {
                System.err.println("         Lỗi: Không thể lấy lại khách hàng vừa thêm bằng ID.");
            }
            // Thử tìm bằng SĐT
            Customer foundByPhone = customerDAO.getCustomerByPhone("0333444555");
             if (foundByPhone != null && foundByPhone.getId() == newCustomerId) {
                System.out.println("         -> Tìm thấy khách hàng bằng SĐT thành công.");
            } else {
                System.err.println("         Lỗi: Không tìm thấy khách hàng bằng SĐT hoặc sai ID.");
            }

        } else {
            System.err.println("      ❌ Them khach hang moi THAT BAI!");
        }

        // Chỉ chạy Update và Delete nếu Add thành công
        if (newCustomerId != -1) {
            // 7.3: Cập nhật khách hàng
            System.out.println("\n   7.3: Cập nhật khách hàng...");
            Customer customerToUpdate = customerDAO.getCustomerById(newCustomerId);
            if (customerToUpdate != null) {
                customerToUpdate.setFullName("Khach Hang Updated");
                customerToUpdate.setPhone("0333444666"); // Đổi SĐT
                boolean updateSuccess = customerDAO.updateCustomer(customerToUpdate);
                if (updateSuccess) {
                    System.out.println("      ✅ Cap nhat thanh cong khach hang ID: " + newCustomerId);
                    Customer updatedCustomer = customerDAO.getCustomerById(newCustomerId);
                    System.out.println("         -> " + updatedCustomer.toString());
                } else {
                    System.err.println("      ❌ Cap nhat khach hang THAT BAI!");
                }
            } else {
                System.err.println("      Lỗi: Không tìm thấy khách hàng ID " + newCustomerId + " để cập nhật.");
            }

            // 7.4: Xóa khách hàng (Thử xóa khách hàng vừa tạo)
            System.out.println("\n   7.4: Xóa khách hàng...");
            boolean deleteSuccess = customerDAO.deleteCustomer(newCustomerId);
            if (deleteSuccess) {
                System.out.println("      ✅ Xoa thanh cong khach hang ID: " + newCustomerId);
                Customer deletedCustomer = customerDAO.getCustomerById(newCustomerId);
                if (deletedCustomer == null) {
                    System.out.println("         -> Khach hang ID " + newCustomerId + " da bi xoa khoi DB.");
                } else {
                    System.err.println("         Lỗi: Van tim thay khach hang sau khi xoa!");
                }
            } else {
                System.err.println("      ❌ Xoa khach hang THAT BAI! (Có thể do khách hàng này đã có đơn hàng)");
            }
        } else {
            System.out.println("\n   BỎ QUA 7.3 & 7.4 do 7.2 thất bại.");
        }
        System.out.println("--- KẾT THÚC BƯỚC 7 ---");
    }
}

