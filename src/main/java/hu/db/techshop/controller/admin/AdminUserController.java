package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.UserDAO;
import hu.db.techshop.model.User;
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
public class AdminUserController {

    @Autowired
    UserDAO userDAO;

    @GetMapping(value = "/admin/users")
    public String index(@RequestParam(name = "q", required = false) String q, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        if (q != null && !q.equals("")) {
            model.addAttribute("userList", userDAO.findAll(q));
        }
        else {
            model.addAttribute("userList", userDAO.findAll());
        }

        return "admin/users/list";
    }


    @GetMapping(value = "/admin/users/create")
    public String create(User user, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        return "admin/users/form";
    }

    @GetMapping(value = "/admin/users/edit/{id}")
    public String edit(@PathVariable int id, User user, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("user", userDAO.findById(id));

        return "admin/users/form";
    }

    @GetMapping(value = "/admin/users/delete/{id}")
    public String edit(@PathVariable int id, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        User user = userDAO.findById(id);

        if (userDAO.delete(user) > 0) {
            return "redirect:/admin/users?success=delete";
        }

        return "redirect:/admin/users?error=delete";
    }

    @PostMapping(value = "/admin/users")
    public String insert(@Valid User user, BindingResult bindingResult, Model model,
                         HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            return "admin/users/form";
        }

        String event = (user.getId() > 0 ? "modify" : "add");

        userDAO.save(user);

        return "redirect:/admin/users?success=" + event;
    }

}
