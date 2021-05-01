package hu.db.techshop.service;

import hu.db.techshop.model.Cart;
import hu.db.techshop.model.Order;

public interface OrderService {

    public Order addOrder(Order order, Cart cart, int userId);

}
