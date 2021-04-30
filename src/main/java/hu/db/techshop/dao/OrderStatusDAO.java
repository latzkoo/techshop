package hu.db.techshop.dao;

import hu.db.techshop.model.OrderStatus;

import java.util.List;

public interface OrderStatusDAO {

    List<OrderStatus> findAll();
    OrderStatus findById(int id);

}
