package hu.db.techshop.dao;

import hu.db.techshop.model.Product;

import java.util.List;

public interface ProductDAO {

    List<Product> findAll(String sort, boolean isActive);
    List<Product> findAll(String sort, int categoryId, boolean isActive);
    List<Product> findAll(String sort, String keyword, boolean isActive);
    List<Product> findSimilar(Product product);
    List<Product> findFreshest(int categoryId);
    Product findById(int id, boolean isActive);
    Product findBySlug(String slug, boolean isActive);
    Product save(Product product);
    void delete(Product product);

}
