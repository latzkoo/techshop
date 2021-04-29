package hu.db.techshop.util;

import hu.db.techshop.model.Cart;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Util {

    public static Map<String, String> cartToMap(Cart cart) {
        Map<String, String> mappedCart = new HashMap<>();

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("hu", "HU"));
        numberFormat.setGroupingUsed(true);

        mappedCart.put("qty", Integer.toString(cart.count()));
        mappedCart.put("value", numberFormat.format(cart.value()));

        return mappedCart;
    }

}
