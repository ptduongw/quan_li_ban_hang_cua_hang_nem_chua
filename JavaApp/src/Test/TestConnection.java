package Test;

// Them import cho Controller
import controller.CustomerController;
import controller.ProductController;
import controller.ProfitController; // <<< THEM IMPORT NAY
import controller.SalesController;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.ProfitDAO;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import model.Customer;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.Profit;
import utils.DatabaseConnector;
import java.util.ArrayList;
import java.sql.SQLException;


/**
 * Lop nay dung de kiem tra ket noi toi database va cac ham CRUD co ban.
 * No khong phai la phan chinh cua ung dung, chi dung de test.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("--- BUOC 1: KIEM TRA KET NOI DATABASE ---");
        Connection conn = DatabaseConnector.getConnection();

        if (conn != null) {
            String dbName = getDatabaseName(conn);
            System.out.println("--------------------------------------------------");
            System.out.println("✅ KET NOI THANH CONG!");
            System.out.println("   Ban da san sang de lam viec voi database '" + dbName + "'.");
            System.out.println("--------------------------------------------------");

            // --- BUOC 2: KIEM TRA ProductDAO.getAllProducts() ---
            testGetAllProducts();

            // --- BUOC 3, 4, 5: KIEM TRA ProductDAO CRUD ---
            testProductCRUD();

            // --- BUOC 6: KIEM TRA OrderDAO.addOrder() ---
            testAddOrder();

            // --- BUOC 7: KIEM TRA CustomerDAO CRUD ---
            testCustomerCRUD();

            // --- BUOC 8: KIEM TRA ProfitDAO CRUD ---
            testProfitCRUD();

            // --- BUOC 9: KIEM TRA SALES CONTROLLER LOGIC ---
            testSalesControllerLogic();

            // --- BUOC 10: KIEM TRA PRODUCT CONTROLLER LOGIC ---
            testProductControllerLogic();
            
            // --- BUOC 11: KIEM TRA CUSTOMER CONTROLLER LOGIC ---
            testCustomerControllerLogic();
            
            // --- BUOC 12: KIEM TRA PROFIT CONTROLLER LOGIC ---
            testProfitControllerLogic(); // <<< GOI HAM TEST MOI

            try {
                conn.close(); // Dong ket noi o cuoi cung
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("--------------------------------------------------");
            System.err.println("❌ KET NOI THAT BAI!");
            System.err.println("   Vui long kiem tra lai DB_URL, USER, va PASS trong file DatabaseConnector.java");
            System.out.println("--------------------------------------------------");
        }
    }

    // Ham lay ten database tu connection (de hien thi)
    private static String getDatabaseName(Connection conn) {
        try {
            return conn.getCatalog();
        } catch (SQLException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    // --- CAC HAM TEST (Cac buoc truoc giu nguyen) ---
    
    public static void testGetAllProducts() {
        System.out.println("\n--- BUOC 2: TEST READ (getAllProducts) ---");
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.getAllProducts();

        if (productList.isEmpty()) {
            System.err.println("   Loi: Khong tim thay san pham nao, hoac co loi khi lay du lieu.");
        } else {
            System.out.println("   Tim thay " + productList.size() + " san pham:");
            for (Product p : productList) {
                System.out.println("      " + p.toString()); // In thong tin san pham
            }
        }
        System.out.println("--- KET THUC BUOC 2 ---");
    }

    public static void testProductCRUD() {
        ProductDAO productDAO = new ProductDAO();
        int newProductId = -1; // Bien de luu ID san pham moi

        // --- BUOC 3: TEST CREATE (addProduct) ---
        System.out.println("\n--- BUOC 3: TEST CREATE (addProduct) ---");
        Product newProduct = new Product("Banh Mi Test DAO", "piece", 15000.0, 50, "Ghi chu test DAO");
        int generatedId = productDAO.addProduct(newProduct);
        if (generatedId != -1) {
            newProductId = generatedId; // Luu lai ID
            System.out.println("   ✅ (DAO) Them thanh cong san pham moi! ID moi la: " + newProductId);
            Product addedProduct = productDAO.getProductById(newProductId);
            if (addedProduct != null) {
                 System.out.println("      -> " + addedProduct.toString());
            } else {
                 System.err.println("      Loi: Khong the lay lai san pham vua them.");
            }
        } else {
            System.err.println("   ❌ (DAO) Them san pham moi THAT BAI!");
        }
        System.out.println("--- KET THUC BUOC 3 ---");

        if (newProductId != -1) {
            // --- BUOC 4: TEST UPDATE (updateProduct) ---
            System.out.println("\n--- BUOC 4: TEST UPDATE (updateProduct) ---");
            Product productToUpdate = productDAO.getProductById(newProductId);
            if (productToUpdate != null) {
                 productToUpdate.setPrice(20000.0);
                 productToUpdate.setQuantity(40);
                 boolean updateSuccess = productDAO.updateProduct(productToUpdate);
                 if (updateSuccess) {
                     System.out.println("   ✅ (DAO) Cap nhat thanh cong san pham ID: " + newProductId);
                     Product updatedProduct = productDAO.getProductById(newProductId);
                     System.out.println("      -> " + updatedProduct.toString());
                 } else {
                     System.err.println("   ❌ (DAO) Cap nhat san pham THAT BAI!");
                 }
            } else {
                 System.err.println("   Loi: Khong tim thay san pham ID " + newProductId + " de cap nhat.");
            }
            System.out.println("--- KET THUC BUOC 4 ---");

            // --- BUOC 5: TEST DELETE (deleteProduct) ---
            System.out.println("\n--- BUOC 5: TEST DELETE (deleteProduct) ---");
            boolean deleteSuccess = productDAO.deleteProduct(newProductId);
            if (deleteSuccess) {
                System.out.println("   ✅ (DAO) Xoa thanh cong san pham ID: " + newProductId);
                Product deletedProduct = productDAO.getProductById(newProductId);
                if (deletedProduct == null) {
                    System.out.println("      -> (DAO) San pham ID " + newProductId + " da bi xoa khoi DB.");
                } else {
                    System.err.println("      Loi: Van tim thay san pham sau khi xoa!");
                }
            } else {
                System.err.println("   ❌ (DAO) Xoa san pham THAT BAI!");
            }
            System.out.println("--- KET THUC BUOC 5 ---");
        } else {
             System.out.println("\n--- BO QUA BUOC 4 & 5 do BUOC 3 that bai ---");
        }
    }

    public static void testAddOrder() {
        System.out.println("\n--- BUOC 6: TEST CREATE ORDER (addOrder) ---");
        OrderDAO orderDAO = new OrderDAO();
        ProductDAO productDAO = new ProductDAO();
        CustomerDAO customerDAO = new CustomerDAO(); // Can de lay khach hang

        // Lay khach hang co ID = 1 (hoac tao moi neu chua co)
        Customer customer = customerDAO.getCustomerById(1);
        if (customer == null) {
             System.out.println("   Khach hang ID 1 khong ton tai, dang thu tao moi...");
             customer = new Customer("0912345678", "Nguyen Van A");
             int newCustId = customerDAO.addCustomer(customer);
             if (newCustId != -1) {
                 customer.setId(newCustId); // Gan ID moi
                 System.out.println("   -> Da tao khach hang moi: " + customer.toString());
             } else {
                 System.err.println("   Loi: Khong the tao khach hang ID 1 de test.");
                 System.out.println("--- KET THUC BUOC 6 (That bai som) ---");
                 return;
             }
        } else {
             System.out.println("   Su dung khach hang co san: " + customer.toString());
        }


        Order newOrder = new Order();
        newOrder.setCustomerId(customer.getId()); // Gan ID khach hang

        List<OrderDetail> details = new ArrayList<>();

        Product product1 = productDAO.getProductById(1);
        if (product1 != null && product1.getQuantity() >= 1) {
             OrderDetail detail1 = new OrderDetail(0, 1, 1, product1.getPrice());
             details.add(detail1);
        } else {
             System.err.println("   Loi: Khong tim thay san pham ID 1 hoac khong du hang (can 1).");
             System.out.println("--- KET THUC BUOC 6 (That bai som) ---");
             return;
        }

        Product product2 = productDAO.getProductById(2);
        if (product2 != null && product2.getQuantity() >= 2) {
            OrderDetail detail2 = new OrderDetail(0, 2, 2, product2.getPrice());
            details.add(detail2);
        } else {
             System.err.println("   Loi: Khong tim thay san pham ID 2 hoac khong du hang (can 2).");
             System.out.println("--- KET THUC BUOC 6 (That bai som) ---");
             return;
        }

        newOrder.setDetails(details);

        double total = 0;
        for (OrderDetail d : newOrder.getDetails()) {
            total += d.getQuantity() * d.getSalePrice();
        }
        newOrder.setTotalAmount(total);

        System.out.println("   Dang thu them don hang cho KH ID " + customer.getId() + " voi tong tien: " + String.format("%,.0f", total));

        int generatedOrderId = orderDAO.addOrder(newOrder);

        if (generatedOrderId != -1) {
            System.out.println("   ✅ Them thanh cong don hang moi! ID don hang la: " + generatedOrderId);
        } else {
            System.err.println("   ❌ Them don hang moi THAT BAI!");
        }
        System.out.println("--- KET THUC BUOC 6 ---");
    }

    public static void testCustomerCRUD() {
        System.out.println("\n--- BUOC 7: TEST CUSTOMER DAO CRUD ---"); // Doi ten de phan biet
        CustomerDAO customerDAO = new CustomerDAO();
        int newCustomerId = -1; // Bien de luu ID khach hang moi

        // 7.1: Lay danh sach khach hang hien co
        System.out.println("   7.1: (DAO) Lay danh sach khach hang hien co...");
        List<Customer> currentCustomers = customerDAO.getAllCustomers();
        if (currentCustomers.isEmpty()) {
            System.out.println("      -> (DAO) Chua co khach hang nao.");
        } else {
            System.out.println("      -> (DAO) Tim thay " + currentCustomers.size() + " khach hang:");
            for (Customer c : currentCustomers) {
                System.out.println("         " + c.toString());
            }
        }

        // 7.2: Them khach hang moi
        System.out.println("\n   7.2: (DAO) Them khach hang moi...");
        // Tao mot SDT ngau nhien de tranh loi UNIQUE KEY
        String randomPhone = "09" + (int) (Math.random() * 100000000);
        Customer newCustomer = new Customer(randomPhone, "Khach Hang Test (DAO)");

        int generatedId = customerDAO.addCustomer(newCustomer);
        if (generatedId != -1) {
            newCustomerId = generatedId;
            System.out.println("      ✅ (DAO) Them thanh cong khach hang moi! ID moi la: " + newCustomerId);
            Customer addedCustomer = customerDAO.getCustomerById(newCustomerId);
            if (addedCustomer != null) {
                System.out.println("         -> " + addedCustomer.toString());
            } else {
                System.err.println("         Loi: Khong the lay lai khach hang vua them bang ID.");
            }
            Customer foundByPhone = customerDAO.getCustomerByPhone(randomPhone);
            if (foundByPhone != null && foundByPhone.getId() == newCustomerId) {
                System.out.println("         -> (DAO) Tim thay khach hang bang SDT thanh cong.");
            } else {
                System.err.println("         Loi: Khong tim thay khach hang bang SDT hoac sai ID.");
            }

        } else {
            System.err.println("      ❌ (DAO) Them khach hang moi THAT BAI!");
        }

        if (newCustomerId != -1) {
            // 7.3: Cap nhat khach hang
            System.out.println("\n   7.3: (DAO) Cap nhat khach hang...");
            Customer customerToUpdate = customerDAO.getCustomerById(newCustomerId);
            if (customerToUpdate != null) {
                customerToUpdate.setFullName("Khach Hang Updated (DAO)");
                customerToUpdate.setPhone(customerToUpdate.getPhone() + "U"); // Doi SDT de tranh trung
                boolean updateSuccess = customerDAO.updateCustomer(customerToUpdate);
                if (updateSuccess) {
                    System.out.println("      ✅ (DAO) Cap nhat thanh cong khach hang ID: " + newCustomerId);
                    Customer updatedCustomer = customerDAO.getCustomerById(newCustomerId);
                    System.out.println("         -> " + updatedCustomer.toString());
                } else {
                    System.err.println("      ❌ (DAO) Cap nhat khach hang THAT BAI!");
                }
            } else {
                System.err.println("      Loi: Khong tim thay khach hang ID " + newCustomerId + " de cap nhat.");
            }

            // 7.4: Xoa khach hang (Thu xoa khach hang vua tao)
            System.out.println("\n   7.4: (DAO) Xoa khach hang...");
            boolean deleteSuccess = customerDAO.deleteCustomer(newCustomerId);
            if (deleteSuccess) {
                System.out.println("      ✅ (DAO) Xoa thanh cong khach hang ID: " + newCustomerId);
                Customer deletedCustomer = customerDAO.getCustomerById(newCustomerId);
                if (deletedCustomer == null) {
                    System.out.println("         -> (DAO) Khach hang ID " + newCustomerId + " da bi xoa khoi DB.");
                } else {
                    System.err.println("         Loi: Van tim thay khach hang sau khi xoa!");
                }
            } else {
                System.err.println("      ❌ (DAO) Xoa khach hang THAT BAI! (Co the do khach hang nay da co don hang)");
            }
        } else {
            System.out.println("\n   (DAO) BO QUA 7.3 & 7.4 do 7.2 that bai.");
        }
        System.out.println("--- KET THUC BUOC 7 ---");
    }

    public static void testProfitCRUD() {
        System.out.println("\n--- BUOC 8: TEST PROFIT CRUD ---");
        ProfitDAO profitDAO = new ProfitDAO();

        // 8.1: Lay danh sach ban ghi loi nhuan hien co
        System.out.println("   8.1: Lay danh sach ban ghi loi nhuan hien co...");
        List<Profit> currentProfits = profitDAO.getAllProfitRecords();
        if (currentProfits.isEmpty()) {
            System.out.println("      -> Chua co ban ghi loi nhuan nao.");
        } else {
            System.out.println("      -> Tim thay " + currentProfits.size() + " ban ghi:");
            for (Profit p : currentProfits) {
                System.out.println("         " + p.toString());
            }
        }

        // 8.2: Them ban ghi loi nhuan moi cho ngay hom nay
        System.out.println("\n   8.2: Them ban ghi loi nhuan moi cho hom nay...");

        double todayRevenue = 750000.0;
        double todayCostMeat = 300000.0;
        double todayCostSpices = 80000.0;
        double todayCostLeaf = 30000.0;
        double todayCostLabor = 100000.0;

        Profit newProfitRecord = new Profit(new Date(), // Lay ngay hien tai
                todayRevenue,
                todayCostMeat,
                todayCostSpices,
                todayCostLeaf,
                todayCostLabor);
        // Loi nhuan rong (netProfit) se tu dong duoc tinh trong constructor cua Profit

        boolean addSuccess = profitDAO.addProfitRecord(newProfitRecord);
        if (addSuccess) {
            System.out.println("      ✅ Them thanh cong ban ghi loi nhuan moi!");
            System.out.println("\n      -> Danh sach loi nhuan sau khi them:");
            List<Profit> updatedProfits = profitDAO.getAllProfitRecords();
            for (Profit p : updatedProfits) {
                System.out.println("         " + p.toString());
            }
        } else {
            System.err.println("      ❌ Them ban ghi loi nhuan moi THAT BAI!");
        }

        System.out.println("--- KET THUC BUOC 8 ---");
    }

    public static void testSalesControllerLogic() {
        System.out.println("\n--- BUOC 9: TEST SALES CONTROLLER LOGIC ---");
        SalesController salesController = new SalesController();
        ProductDAO productDAO = new ProductDAO(); // Can DAO de lay san pham cho gio hang

        // 9.1: Test ham getAllProducts cua Controller
        System.out.println("   9.1: Test controller.getAllProducts()...");
        List<Product> products = salesController.getAllProducts();
        if (!products.isEmpty()) {
            System.out.println("      ✅ (SalesController) Da lay duoc " + products.size() + " san pham.");
        } else {
            System.err.println("      ❌ (SalesController) KHONG lay duoc san pham.");
        }

        // 9.2: Test ham findCustomerByPhone cua Controller
        System.out.println("\n   9.2: Test controller.findCustomerByPhone()...");
        Customer cust1 = salesController.findCustomerByPhone("0912345678"); // Gia su KH nay ton tai
        if (cust1 != null) {
            System.out.println("      ✅ (SalesController) Da tim thay KH co san: " + cust1.getFullName());
        } else {
            System.err.println("      (i) (SalesController) Khong tim thay KH 0912345678 (Co the do KH nay chua duoc tao o BUOC 6 hoac 7)");
        }

        // 9.3: Test ham processPayment voi KHACH HANG MOI (Logic quan trong)
        System.out.println("\n   9.3: Test controller.processPayment() voi KHACH HANG MOI...");

        // Tao mot khach hang moi (chua co ID, ID = 0)
        String randomPhone = "0555" + (int) (Math.random() * 1000000); // SDT ngau nhien
        Customer newCustomer = new Customer(randomPhone, "Khach Moi Tu Controller");

        // Tao gio hang (gia lap View)
        List<OrderDetail> cart = new ArrayList<>();
        Product p3 = productDAO.getProductById(3); // Lay "Fried crab spring roll"

        if (p3 != null && p3.getQuantity() >= 1) {
            cart.add(new OrderDetail(0, p3.getId(), 1, p3.getPrice()));
            double total = p3.getPrice() * 1;

            System.out.println("   -> (SalesController) Gia lap thanh toan cho KH moi: " + newCustomer.getFullName() + " | Gio hang: 1 x " + p3.getName());

            // Goi ham "bo nao"
            int newOrderId = salesController.processPayment(newCustomer, cart, total);

            if (newOrderId != -1) {
                System.out.println("   ✅✅✅ TEST SALES CONTROLLER THANH CONG! ✅✅✅");
                System.out.println("      -> (SalesController) Da tu dong them KH moi (ID: " + newCustomer.getId() + ")");
                System.out.println("      -> (SalesController) Da tao don hang moi (ID: " + newOrderId + ")");
                System.out.println("      -> (SalesController) KIEM TRA MYSQL WORKBENCH: Phai thay KH moi, Don hang moi, va so luong SP ID 3 bi giam.");
            } else {
                System.err.println("   ❌❌❌ TEST SALES CONTROLLER THAT BAI! ❌❌❌");
            }

        } else {
            System.err.println("   Loi: Khong tim thay san pham ID 3 hoac het hang de test controller.");
        }

        System.out.println("--- KET THUC BUOC 9 ---");
    }

    public static void testProductControllerLogic() {
        System.out.println("\n--- BUOC 10: TEST PRODUCT CONTROLLER LOGIC ---");
        ProductController productController = new ProductController();
        int newProductId = -1; // Bien de luu ID san pham moi

        // 10.1: Test addProduct (Thanh cong)
        System.out.println("   10.1: Test controller.addProduct (Thanh cong)...");
        int generatedId = productController.addProduct("Test tu Controller", "cai", 1000.0, 10, "Ghi chu Controller");

        if (generatedId != -1) {
            newProductId = generatedId; // Luu lai ID
            System.out.println("      ✅ (ProductController) Them thanh cong san pham moi! ID moi la: " + newProductId);
        } else {
            System.err.println("      ❌ (ProductController) Them san pham moi THAT BAI!");
        }

        // 10.2: Test addProduct (That bai - Ten rong)
        System.out.println("\n   10.2: Test controller.addProduct (That bai - Ten rong)...");
        int failId1 = productController.addProduct("", "cai", 1000.0, 10, ""); // Ten rong
        if (failId1 == -1) {
            System.out.println("      ✅ (ProductController) Da chan them san pham ten rong thanh cong!");
        } else {
            System.err.println("      ❌ (ProductController) LOI: Dang le phai chan san pham ten rong!");
        }

        // 10.3: Test addProduct (That bai - Gia am)
        System.out.println("\n   10.3: Test controller.addProduct (That bai - Gia am)...");
        int failId2 = productController.addProduct("Test Gia Am", "cai", -100.0, 10, ""); // Gia am
        if (failId2 == -1) {
            System.out.println("      ✅ (ProductController) Da chan them san pham gia am thanh cong!");
        } else {
            System.err.println("      ❌ (ProductController) LOI: Dang le phai chan san pham gia am!");
        }

        // 10.4 & 10.5: Test Update va Delete (Chi khi 10.1 thanh cong)
        if (newProductId != -1) {
            // 10.4: Test updateProduct
            System.out.println("\n   10.4: Test controller.updateProduct...");
            boolean updateSuccess = productController.updateProduct(newProductId, "Ten Da Cap Nhat (Controller)", "kg", 500.0, 5, "Da sua tu Controller");
            if (updateSuccess) {
                System.out.println("      ✅ (ProductController) Cap nhat thanh cong san pham ID: " + newProductId);
                // In ra de kiem tra
                Product updatedProduct = productController.getProductById(newProductId);
                System.out.println("         -> " + updatedProduct.toString());
            } else {
                System.err.println("      ❌ (ProductController) Cap nhat san pham THAT BAI!");
            }

            // 10.5: Test deleteProduct
            System.out.println("\n   10.5: Test controller.deleteProduct...");
            boolean deleteSuccess = productController.deleteProduct(newProductId);
            if (deleteSuccess) {
                System.out.println("      ✅ (ProductController) Xoa thanh cong san pham ID: " + newProductId);
                Product deletedProduct = productController.getProductById(newProductId);
                if (deletedProduct == null) {
                    System.out.println("         -> (ProductController) San pham ID " + newProductId + " da bi xoa khoi DB.");
                } else {
                    System.err.println("         Loi: Van tim thay san pham sau khi xoa!");
                }
            } else {
                System.err.println("      ❌ (ProductController) Xoa san pham THAT BAI!");
            }
        } else {
            System.out.println("\n   (ProductController) BO QUA 10.4 & 10.5 do 10.1 that bai.");
        }

        System.out.println("--- KET THUC BUOC 10 ---");
    }
    
    public static void testCustomerControllerLogic() {
        System.out.println("\n--- BUOC 11: TEST CUSTOMER CONTROLLER LOGIC ---");
        CustomerController customerController = new CustomerController();
        int newCustomerId = -1; // Bien de luu ID
        String randomPhone = "0666" + (int) (Math.random() * 1000000); // SDT ngau nhien moi

        // 11.1: Test addCustomer (That bai - Thieu thong tin)
        System.out.println("   11.1: Test controller.addCustomer (That bai - Thieu thong tin)...");
        int failId1 = customerController.addCustomer("", "Ten Day Du"); // Thieu SDT
        if (failId1 == -1) {
            System.out.println("      ✅ (CustomerController) Da chan them KH thieu SDT thanh cong!");
        } else {
            System.err.println("      ❌ (CustomerController) LOI: Dang le phai chan KH thieu SDT!");
        }

        // 11.2: Test addCustomer (Thanh cong)
        System.out.println("\n   11.2: Test controller.addCustomer (Thanh cong)...");
        int generatedId = customerController.addCustomer(randomPhone, "Khach Test (Controller)");
        if (generatedId > 0) { // addCustomer tra ve ID
            newCustomerId = generatedId;
            System.out.println("      ✅ (CustomerController) Them thanh cong KH moi! ID moi la: " + newCustomerId);
        } else {
            System.err.println("      ❌ (CustomerController) Them KH moi THAT BAI! (Tra ve " + generatedId + ")");
        }

        // 11.3: Test addCustomer (That bai - Trung SDT)
        System.out.println("\n   11.3: Test controller.addCustomer (That bai - Trung SDT)...");
        int failId2 = customerController.addCustomer(randomPhone, "Nguoi Khac Trung SDT"); // Trung SDT vua tao
        if (failId2 == -2) { // Ma loi -2 la trung SDT
            System.out.println("      ✅ (CustomerController) Da chan them KH trung SDT thanh cong!");
        } else {
            System.err.println("      ❌ (CustomerController) LOI: Dang le phai chan KH trung SDT! (Tra ve " + failId2 + ")");
        }
        
        // 11.4 & 11.5: Test Update va Delete (Chi khi 11.2 thanh cong)
        if (newCustomerId != -1) {
            // 11.4: Test updateCustomer
            System.out.println("\n   11.4: Test controller.updateCustomer...");
            boolean updateSuccess = customerController.updateCustomer(newCustomerId, randomPhone + "U", "Ten Da Cap Nhat (Controller)");
            if (updateSuccess) {
                System.out.println("      ✅ (CustomerController) Cap nhat thanh cong KH ID: " + newCustomerId);
                // In ra de kiem tra
                Customer updatedCustomer = customerController.findCustomerByPhone(randomPhone + "U");
                System.out.println("         -> " + updatedCustomer.toString());
            } else {
                System.err.println("      ❌ (CustomerController) Cap nhat KH THAT BAI!");
            }

            // 11.5: Test deleteCustomer
            System.out.println("\n   11.5: Test controller.deleteCustomer...");
            boolean deleteSuccess = customerController.deleteCustomer(newCustomerId);
            if (deleteSuccess) {
                System.out.println("      ✅ (CustomerController) Xoa thanh cong KH ID: " + newCustomerId);
                Customer deletedCustomer = customerController.findCustomerByPhone(randomPhone + "U");
                 if (deletedCustomer == null) {
                    System.out.println("         -> (CustomerController) KH ID " + newCustomerId + " da bi xoa khoi DB.");
                } else {
                    System.err.println("         Loi: Van tim thay KH sau khi xoa!");
                }
            } else {
                System.err.println("      ❌ (CustomerController) Xoa KH THAT BAI! (Co the do KH nay da co don hang)");
            }
        } else {
             System.out.println("\n   (CustomerController) BO QUA 11.4 & 11.5 do 11.2 that bai.");
        }

        System.out.println("--- KET THUC BUOC 11 ---");
    }
    
    // --- HAM TEST MOI CHO PROFITCONTROLLER ---
    public static void testProfitControllerLogic() {
        System.out.println("\n--- BUOC 12: TEST PROFIT CONTROLLER LOGIC ---");
        ProfitController profitController = new ProfitController();

        // 12.1: Test addProfitRecord (That bai - So am)
        System.out.println("   12.1: Test controller.addProfitRecord (That bai - So am)...");
        // Su dung chi phi am de kiem tra validation
        boolean failAdd = profitController.addProfitRecord(new Date(), 500.0, -10.0, 0, 0, 0); 
        if (!failAdd) { // Ham tra ve false la dung
            System.out.println("      ✅ (ProfitController) Da chan them ban ghi co so am thanh cong!");
        } else {
            System.err.println("      ❌ (ProfitController) LOI: Dang le phai chan ban ghi co so am!");
        }

        // 12.2: Test addProfitRecord (Thanh cong)
        System.out.println("\n   12.2: Test controller.addProfitRecord (Thanh cong)...");
        boolean addSuccess = profitController.addProfitRecord(new Date(), 1000000.0, 400000.0, 50000.0, 50000.0, 100000.0);
        if (addSuccess) {
            System.out.println("      ✅ (ProfitController) Them thanh cong ban ghi loi nhuan moi!");
            
            // 12.3: Test getAllProfitRecords
            System.out.println("\n   12.3: Test controller.getAllProfitRecords...");
            List<Profit> profitRecords = profitController.getAllProfitRecords();
            if (!profitRecords.isEmpty()) {
                System.out.println("      ✅ (ProfitController) Lay duoc " + profitRecords.size() + " ban ghi loi nhuan.");
                // In ban ghi cuoi cung (la ban ghi moi nhat)
                // ProfitDAO sap xep DESC nen get(0) la moi nhat
                System.out.println("         -> Ban ghi moi nhat: " + profitRecords.get(0).toString()); 
            } else {
                System.err.println("      ❌ (ProfitController) Khong lay duoc ban ghi loi nhuan.");
            }
        } else {
            System.err.println("      ❌ (ProfitController) Them ban ghi loi nhuan moi THAT BAI!");
        }
        
        System.out.println("--- KET THUC BUOC 12 ---");
    }
}

