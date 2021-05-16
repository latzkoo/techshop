package hu.db.techshop.dao;

import hu.db.techshop.model.Category;

import java.util.List;

public interface CategoryDAO {

    List<Category> findAll();
    List<Category> findAll(int status);
    List<Category> findAll(String keyword);
    List<Category> findAll(int status, String keyword);
    Category findById(int id);
    Category findBySlug(String slug);
    Category save(Category category);
    int delete(Category category);

}
