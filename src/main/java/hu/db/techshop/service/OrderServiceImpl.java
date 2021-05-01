package hu.db.techshop.service;

import hu.db.techshop.dao.OrderDAO;
import hu.db.techshop.dao.OrderItemDAO;
import hu.db.techshop.mapper.OrderItemMapper;
import hu.db.techshop.model.Cart;
import hu.db.techshop.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderItemDAO orderItemDAO;

    @Transactional
    public Order addOrder(Order order, Cart cart, int userId) {
        OrderItemMapper mapper = new OrderItemMapper();

        order =  orderDAO.save(order, userId);
        orderItemDAO.saveAll(order.getId(), mapper.mapToOrderItem(cart.getItems()));

        cart.empty();

        return order;
    }

}
