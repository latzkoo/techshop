package hu.db.techshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductController {

    @GetMapping(value = "/termekek")
    public String index(Model model, HttpServletRequest request) {
        return "products/list";
    }

    @GetMapping(value = "/termekek/{category}")
    public String category(@PathVariable String category, Model model, HttpServletRequest request) {
        return "products/list";
    }

    @GetMapping(value = "/termekek/{category}/{slug}")
    public String get(@PathVariable String category, @PathVariable String slug, Model model, HttpServletRequest request) {
        return "products/product";
    }

}
