package hu.db.techshop.controller;

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
    private UserDAO userDAO;

    @GetMapping(value = "/belepes")
    public String index(Model model, HttpServletRequest request) {
        return "login";
    }

    @PostMapping(value = "/belepes")
    public String loginSuccess(@RequestParam("email") String email, @RequestParam("passwd") String passwd,
                               @RequestParam(value = "referer", required = false) String referer,
                               HttpServletRequest request, Model model) {
        User user = userDAO.getUserByEmailAndPassword(email, passwd);

        if (user == null) {
            return "redirect:/belepes?error";
        }

        request.getSession().setAttribute("USERID", user.getId());

        return "redirect:" + (referer != null ? referer : '/');
    }

    @GetMapping(value = "/kilepes")
    public String logout(Model model, HttpServletRequest request) {
        request.getSession().removeAttribute("USERID");

        return "redirect:/";
    }

}
