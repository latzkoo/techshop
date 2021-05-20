package hu.db.techshop.dao;

import hu.db.techshop.model.Order;

import java.util.List;

public interface OrderDAO {

    List<Order> findAll(int statusId);
    List<Order> findAll(int statusId, String keyword);
    List<Order> findAllByUserId(int id);
    Order findById(int id);
    Order save(Order order, int userId);
    void setStatus(int id, int statusId);
    void delete(int orderId);

}
