package hu.db.techshop.dao;

import hu.db.techshop.model.Order;

import java.util.List;

public interface OrderDAO {

    List<Order> findAll();
    Order findById(int id);
    Order save(Order order, int userId);
    void delete(Order order);

}
