package hu.db.techshop.controller;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.OrderDAO;
import hu.db.techshop.dao.UserDAO;
import hu.db.techshop.model.Cart;
import hu.db.techshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    Cart cart;

    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/fiokom")
    public String account(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return "redirect:/belepes";
        }

        model.addAttribute("categoryList", categoryDAO.findAll());
        model.addAttribute("user", userDAO.findById((int) request.getSession().getAttribute("USERID")));
        model.addAttribute("orders", orderDAO.findAllByUserId((int) request.getSession().getAttribute("USERID")));

        return "account/account";
    }

    @GetMapping(value = "/fiokom/adatmodositas")
    public String data(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return "redirect:/belepes";
        }

        model.addAttribute("categoryList", categoryDAO.findAll());
        model.addAttribute("user", userDAO.findById((int) request.getSession().getAttribute("USERID")));

        return "account/data";
    }

    @GetMapping(value = "/fiokom/jelszomodositas")
    public String password(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return "redirect:/belepes";
        }

        model.addAttribute("categoryList", categoryDAO.findAll());

        return "account/password";
    }

    @PostMapping(value = "/fiokom/adatmodositas")
    public String saveData(@Valid User user, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return "redirect:/belepes";
        }

        if (bindingResult.hasErrors()) {
            return "account/data";
        }

        user = userDAO.save(user);
        request.getSession().setAttribute("FIRSTNAME", user.getFirstname());

        return "redirect:/fiokom";
    }

    @PostMapping(value = "/fiokom/jelszomodositas")
    public String savePassword(@RequestParam("currentPassword") String currentPassword,
                               @RequestParam("password") String password,
                               @RequestParam("passwordConfirmation") String passwordConfirmation,
                               Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return "redirect:/belepes";
        }

        User user = userDAO.findById((int) request.getSession().getAttribute("USERID"));

        passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "redirect:/fiokom/jelszomodositas?error=current";
        }
        else if (!password.equals(passwordConfirmation)) {
            return "redirect:/fiokom/jelszomodositas?error=3";
        }

        user.setPassword(password);
        userDAO.updatePassword(user);

        return "redirect:/fiokom";
    }

}
