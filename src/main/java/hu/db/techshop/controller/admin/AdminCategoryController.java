package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.model.Category;
import hu.db.techshop.util.Slug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class AdminCategoryController {

    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping(value = "/admin/categories")
    public String index(@RequestParam(name = "q", required = false) String q, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        if (q != null && !q.equals("")) {
            model.addAttribute("categoryList", categoryDAO.findAll(q));
        }
        else {
            model.addAttribute("categoryList", categoryDAO.findAll());
        }

        return "admin/categories/list";
    }

    @GetMapping(value = "/admin/categories/create")
    public String create(Category category, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        return "admin/categories/form";
    }

    @GetMapping(value = "/admin/categories/edit/{id}")
    public String edit(@PathVariable int id, Category category, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("category", categoryDAO.findById(id));

        return "admin/categories/form";
    }

    @GetMapping(value = "/admin/categories/delete/{id}")
    public String edit(@PathVariable int id, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        Category category = categoryDAO.findById(id);

        if (categoryDAO.delete(category) > 0) {
            return "redirect:/admin/categories?success=delete";
        }

        return "redirect:/admin/categories?error=delete";
    }

    @PostMapping(value = "/admin/categories")
    public String insert(@Valid Category category, BindingResult bindingResult, Model model,
                         HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        category.setSlug(Slug.from(category.getCategoryName()));
        categoryDAO.save(category);

        return "redirect:/admin/categories?success=" + (category.getId() > 0 ? "modify" : "add");
    }

}
