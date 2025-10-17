package Service;

import model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
    }

    public List<Product> getAll() {
        return products;
    }

    public Product findById(String id) {
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public void removeProduct(String id) {
        products.removeIf(p -> p.getId().equalsIgnoreCase(id));
    }
}
