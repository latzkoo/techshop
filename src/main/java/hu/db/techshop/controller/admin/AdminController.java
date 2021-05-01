package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.ProductDAO;
import hu.db.techshop.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

    @GetMapping(value = "/admin")
    public String index(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        return "redirect:/admin/products";
    }

}
