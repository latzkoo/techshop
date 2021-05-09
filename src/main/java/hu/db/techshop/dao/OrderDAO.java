package hu.db.techshop.dao;

import hu.db.techshop.model.Order;

import java.util.List;

public interface OrderDAO {

    List<Order> findAll();
    List<Order> findAll(String keyword);
    Order findById(int id);
    Order save(Order order, int userId);
    void delete(int orderId);

}
