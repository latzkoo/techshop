package hu.db.techshop.dao;

import hu.db.techshop.model.Product;

import java.util.List;

public interface ProductDAO {

    List<Product> findAll();
    Product findById(int id);
    Product findBySlug(String slug);
    Product save(Product product);
    void delete(Product product);

}
