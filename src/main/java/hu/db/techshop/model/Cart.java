package hu.db.techshop.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class Cart implements Serializable {

    List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(List<CartItem> cartItems) {
        this.items = cartItems;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> add(Product product, int qty) {
        if (!has(product)) {
            items.add(new CartItem(product, qty));
        }
        else {
            ListIterator<CartItem> iterator = items.listIterator();

            while (iterator.hasNext()) {
                CartItem item = iterator.next();
                if (item.getProduct().getId() == product.getId()) {
                    iterator.set(new CartItem(product, item.getQty() + qty));
                }
            }
        }

        return items;
    }

    public List<CartItem> update(Product product, int qty) {
        ListIterator<CartItem> iterator = items.listIterator();

        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId() == product.getId()) {
                iterator.set(new CartItem(product, qty));
            }
        }

        return items;
    }

    public void empty() {
        items.clear();
    }

    public void remove(int productId) {
        items.removeIf(cartItem -> cartItem.getProduct().getId() == productId);
    }

    public int value() {
        int value = 0;

        for (CartItem item : items) {
            value += item.getProduct().getPrice() * item.getQty();
        }

        return value;
    }

    public int count() {
        int count = 0;

        for (CartItem item : items) {
            count += item.getQty();
        }

        return count;
    }

    private boolean has(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) return true;
        }

        return false;
    }

}
