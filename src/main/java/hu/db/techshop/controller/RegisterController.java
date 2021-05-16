package hu.db.techshop.controller;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.UserDAO;
import hu.db.techshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    UserDAO userDAO;

    @GetMapping(value = "/regisztracio")
    public String index(Model model, HttpServletRequest request, User user) {
        model.addAttribute("categoryList", categoryDAO.findAll());

        return "register";
    }

    @PostMapping(value = "/regisztracio")
    public String register(@Valid User user, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        user = userDAO.save(user);
        request.getSession().setAttribute("USERID", user.getId());

        return "redirect:/";
    }

}
