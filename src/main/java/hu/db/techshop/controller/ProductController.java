package hu.db.techshop.controller;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.ProductDAO;
import hu.db.techshop.model.Category;
import hu.db.techshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ProductDAO productDAO;

    @GetMapping(value = "/termekek")
    public String index(@RequestParam(name = "sort", required = false) String sort,  Model model, HttpServletRequest request) {
        model.addAttribute("categoryList", categoryDAO.findAll());
        model.addAttribute("productList", productDAO.findAll(sort, true));

        return "products/list";
    }

    @GetMapping(value = "/termekek/{slug}")
    public String category(@RequestParam(name = "sort", required = false) String sort,
                           @PathVariable String slug, Model model, HttpServletRequest request) {
        Category category = categoryDAO.findBySlug(slug);
        model.addAttribute("categoryList", categoryDAO.findAll());
        model.addAttribute("category", category);
        model.addAttribute("productList", productDAO.findAll(sort, category.getId(), true));

        return "products/list";
    }

    @GetMapping(value = "/termekek/{category}/{slug}")
    public String get(@PathVariable String category, @PathVariable String slug, Model model, HttpServletRequest request) {
        Product product = productDAO.findBySlug(slug, true);

        if (product == null) {
            return "404";
        }

        model.addAttribute("category", categoryDAO.findBySlug(category));
        model.addAttribute("product", product);
        model.addAttribute("similar", productDAO.findSimilar(product));

        return "products/product";
    }

    @GetMapping(value = "/kereses")
    public String search(@RequestParam(name = "sort", required = false) String sort,
                         @RequestParam(name = "q", required = false) String keyword,
                         Model model, HttpServletRequest request) {
        if (keyword != null && !keyword.equals("")) {
            model.addAttribute("productList", productDAO.findAll(sort, keyword, true));
        }

        return "products/search";
    }

}
