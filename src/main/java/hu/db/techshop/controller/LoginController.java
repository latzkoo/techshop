package hu.db.techshop.controller;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.UserDAO;
import hu.db.techshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "/belepes")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("categoryList", categoryDAO.findAll());

        return "login";
    }

    @PostMapping(value = "/belepes")
    public String loginSuccess(@RequestParam("email") String email, @RequestParam("passwd") String passwd,
                               @RequestParam(value = "referer", required = false) String referer,
                               HttpServletRequest request, Model model) {
        User user = userDAO.getUserByEmailAndPassword(email, passwd, false);

        if (user == null) {
            return "redirect:/belepes?error";
        }

        request.getSession().setAttribute("USERID", user.getId());
        request.getSession().setAttribute("FIRSTNAME", user.getFirstname());

        if (user.isAdmin()) {
            request.getSession().setAttribute("ADMIN", 1);
        }

        return "redirect:" + (referer != null ? referer : '/');
    }

    @GetMapping(value = "/kilepes")
    public String logout(Model model, HttpServletRequest request) {
        request.getSession().removeAttribute("USERID");
        request.getSession().removeAttribute("FIRSTNAME");
        request.getSession().removeAttribute("ADMIN");

        return "redirect:/";
    }

}
