package hu.db.techshop.dao;

import hu.db.techshop.model.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();
    List<User> findAll(String keyword);
    User findById(int id);
    User save(User user);
    void delete(User user);
    User getUserByEmailAndPassword(String email, String password, boolean checkAdmin);

}
