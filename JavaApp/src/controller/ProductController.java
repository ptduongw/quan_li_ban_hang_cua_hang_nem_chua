package controller;

import dao.ProductDAO;
import model.Product;
import java.util.List;

public class ProductController {

    private ProductDAO productDAO;

    public ProductController() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
 
    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }


    public int addProduct(String name, String unit, double price, int quantity, String note) {
        //Kiem tra du lieu dau vao
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Loi Controller: Ten san pham khong duoc de trong.");
            return -1; 
        }
        if (price <= 0) {
            System.err.println("Loi Controller: Gia san pham phai lon hon 0.");
            return -1;
        }
        if (quantity < 0) {
            System.err.println("Loi Controller: So luong khong duoc la so am.");
            return -1;
        }
        
        Product product = new Product(name.trim(), unit, price, quantity, note);
        
        //Lưu
        return productDAO.addProduct(product);
    }

    public boolean updateProduct(int id, String name, String unit, double price, int quantity, String note) {
         //Kiem tra du lieu dau vao
         if (id <= 0) {
             System.err.println("Loi Controller: ID san pham khong hop le.");
             return false;
         }
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Loi Controller: Ten san pham khong duoc de trong.");
            return false;
        }
        if (price <= 0) {
            System.err.println("Loi Controller: Gia san pham phai lon hon 0.");
            return false;
        }
        
        Product product = new Product(id, name.trim(), unit, price, quantity, note);
        
        //Lưu
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int id) {
        if (id <= 0) {
             System.err.println("Loi Controller: ID san pham khong hop le.");
            return false;
        }
        return productDAO.deleteProduct(id);
    }
}

