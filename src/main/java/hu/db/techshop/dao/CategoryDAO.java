package hu.db.techshop.dao;

import hu.db.techshop.model.Category;

import java.util.List;

public interface CategoryDAO {

    List<Category> findAll();
    Category findById(int id);
    Category save(Category category);
    void delete(Category category);

}
