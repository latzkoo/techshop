package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.ContentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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

}
