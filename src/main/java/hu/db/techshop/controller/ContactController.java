package hu.db.techshop.controller;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.ContentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContactController {

    @Autowired
    ContentDAO contentDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping(value = "/kapcsolat")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("categoryList", categoryDAO.findAll());
        model.addAttribute("content", contentDAO.findById(1));

        return "contact";
    }

}
