package hu.db.techshop.controller.admin;

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
public class AdminLoginController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "/admin/login")
    public String index(Model model, HttpServletRequest request) {
        return "admin/login";
    }

    @PostMapping(value = "/admin/login")
    public String loginSuccess(@RequestParam("email") String email, @RequestParam("passwd") String passwd,
                               @RequestParam(value = "referer", required = false) String referer,
                               HttpServletRequest request, Model model) {
        User user = userDAO.getUserByEmailAndPassword(email, passwd, true);

        if (user == null) {
            return "redirect:/admin/login?error";
        }

        request.getSession().setAttribute("USERID", user.getId());
        request.getSession().setAttribute("FIRSTNAME", user.getFirstname());

        if (user.isAdmin()) {
            request.getSession().setAttribute("ADMIN", 1);
        }

        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/logout")
    public String logout(Model model, HttpServletRequest request) {
        request.getSession().removeAttribute("USERID");
        request.getSession().removeAttribute("FIRSTNAME");
        request.getSession().removeAttribute("ADMIN");

        return "redirect:/admin/login";
    }

}
