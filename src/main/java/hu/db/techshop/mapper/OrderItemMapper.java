package hu.db.techshop.mapper;

import hu.db.techshop.model.CartItem;
import hu.db.techshop.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {

    public List<OrderItem> mapToOrderItem(List<CartItem> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderItem(cartItem.getProduct().getId(), cartItem.getProduct().getPrice(), cartItem.getQty()));
        }

        return orderItems;
    }

}
