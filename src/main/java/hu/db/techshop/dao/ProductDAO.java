package hu.db.techshop.dao;

import hu.db.techshop.model.Product;

import java.util.List;

public interface ProductDAO {

    List<Product> findAll(String sort);
    List<Product> findAll(String sort, int categoryId);
    List<Product> findAll(String sort, String keyword);
    List<Product> findSimilar(Product product);
    List<Product> findFreshest(int categoryId);
    Product findById(int id);
    Product findBySlug(String slug);
    Product save(Product product);
    void delete(Product product);

}
