package hu.db.techshop.controller;

import hu.db.techshop.dao.UserDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    UserDAOImpl userDAO;

    @GetMapping(value = "/")
    public String index(Model model, HttpServletRequest request) {
//        if (request.getSession().getAttribute("USERID") != null) {
//            User user = userDAO.findById((int) request.getSession().getAttribute("USERID"));
//        }

        return "index";
    }

}
