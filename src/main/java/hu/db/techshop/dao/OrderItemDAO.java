package hu.db.techshop.dao;

import hu.db.techshop.model.OrderItem;

import java.util.List;

public interface OrderItemDAO {

    List<OrderItem> findAll(int orderId);
    OrderItem findById(int id);
    OrderItem save(int orderId, OrderItem orderItem);
    void saveAll(int orderId, List<OrderItem> orderItems);
    void deleteAll(int orderId);
    void delete(OrderItem orderItem);

}
