package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminOrderController {

    @Autowired
    OrderDAO orderDAO;

    @GetMapping(value = "/admin/orders")
    public String index(@RequestParam(name = "q", required = false) String q, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        if (q != null && !q.equals("")) {
            model.addAttribute("orderList", orderDAO.findAll(q));
        }
        else {
            model.addAttribute("orderList", orderDAO.findAll());
        }

        return "admin/orders/list";
    }

    @GetMapping(value = "/admin/orders/delete/{id}")
    public String delete(@PathVariable int id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        orderDAO.delete(id);

        return "admin/orders/list";
    }

}
