package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.ContentDAO;
import hu.db.techshop.model.Category;
import hu.db.techshop.model.Content;
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
public class AdminContentController {

    @Autowired
    ContentDAO contentDAO;

    @GetMapping(value = "/admin/contents")
    public String index(@RequestParam(name = "q", required = false) String q, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        if (q != null && !q.equals("")) {
            model.addAttribute("contentList", contentDAO.findAll(q));
        }
        else {
            model.addAttribute("contentList", contentDAO.findAll());
        }

        return "admin/contents/list";
    }

    @GetMapping(value = "/admin/contents/edit/{id}")
    public String edit(@PathVariable int id, Category category, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("content", contentDAO.findById(id));

        return "admin/contents/form";
    }

    @PostMapping(value = "/admin/contents")
    public String insert(@Valid Content content, BindingResult bindingResult, Model model,
                         HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        contentDAO.save(content);

        return "redirect:/admin/contents?success=modify";
    }

}
