package hu.db.techshop.controller;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.ProductDAO;
import hu.db.techshop.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    Cart cart;

    @GetMapping(value = "/")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("categoryList", categoryDAO.findAll());

        return "index";
    }

}
