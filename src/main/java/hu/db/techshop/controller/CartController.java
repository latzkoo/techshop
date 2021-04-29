package hu.db.techshop.controller;

import hu.db.techshop.dao.ProductDAO;
import hu.db.techshop.model.Cart;
import hu.db.techshop.model.JSONResponse;
import hu.db.techshop.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CartController {

    @Autowired
    Cart cart;

    @Autowired
    ProductDAO productDAO;

    @PostMapping(value = "/kosar/add/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONResponse add(@PathVariable int productId, @RequestParam int qty, HttpServletRequest request) {
        cart.add(productDAO.findById(productId), qty);

        return new JSONResponse("OK", "SUCCESS", Util.cartToMap(cart));
    }

    @PostMapping(value = "/kosar/update/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONResponse update(@PathVariable int productId, @RequestParam int qty, HttpServletRequest request) {
        cart.update(productDAO.findById(productId), qty);

        return new JSONResponse("OK", "SUCCESS", Util.cartToMap(cart));
    }

}
