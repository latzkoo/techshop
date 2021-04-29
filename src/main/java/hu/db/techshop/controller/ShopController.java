package hu.db.techshop.controller;

import hu.db.techshop.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ShopController {

    @Autowired
    Cart cart;

    @GetMapping(value = "/kosar")
    public String cart(@RequestParam(name = "delete", required = false) String productId,
                       Model model, HttpServletRequest request) {
        if (productId != null) {
            cart.remove(Integer.parseInt(productId));
            return "redirect:/kosar";
        }

        return "cart";
    }

    @GetMapping(value = "/penztar")
    public String checkout(Model model, HttpServletRequest request) {
        return "cassa";
    }

}
