package SaleApp;

import Model.Product;
import Service.ProductService;
import Service.OrderService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductService ps = new ProductService();
        OrderService os = new OrderService();

        // Khởi tạo sản phẩm sẵn trong kho
        ps.addProduct(new Product("P1", "Nem chua", 5000, 100));
        ps.addProduct(new Product("P2", "Giò", 120000, 20));
        ps.addProduct(new Product("P3", "Chả", 90000, 15));
        ps.addProduct(new Product("P4", "Mọc", 70000, 10));

        int choice;
        do {
            System.out.println("\n===== MENU QUẢN LÝ BÁN HÀNG =====");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Ghi nhận đơn hàng");
            System.out.println("5. Xem báo cáo bán hàng");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = sc.nextInt();
            sc.nextLine(); // bỏ dòng trống

            switch (choice) {
                case 1 -> {
                    System.out.println("===== DANH SÁCH SẢN PHẨM =====");
                    for (Product p : ps.getAll()) {
                        System.out.println(p);
                    }
                }
                case 2 -> {
                    System.out.print("Nhập ID sản phẩm: ");
                    String id = sc.nextLine();
                    System.out.print("Tên sản phẩm: ");
                    String name = sc.nextLine();
                    System.out.print("Giá: ");
                    double price = sc.nextDouble();
                    System.out.print("Số lượng: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();
                    ps.addProduct(new Product(id, name, price, quantity));
                    System.out.println("Thêm sản phẩm thành công!");
                }
                case 3 -> {
                    System.out.print("Nhập ID sản phẩm cần xóa: ");
                    String id = sc.nextLine();
                    ps.removeProduct(id);
                    System.out.println("Đã xóa sản phẩm!");
                }
                case 4 -> {
                    System.out.print("Nhập ID sản phẩm bán: ");
                    String id = sc.nextLine();
                    Product p = ps.findById(id);
                    if (p == null) {
                        System.out.println("Không tìm thấy sản phẩm!");
                        break;
                    }
                    System.out.print("Số lượng bán: ");
                    int qty = sc.nextInt();
                    sc.nextLine();
                    os.addOrder(p, qty);
                }
                case 5 -> os.showOrders();
                case 0 -> System.out.println("Tạm biệt!");
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }
}
