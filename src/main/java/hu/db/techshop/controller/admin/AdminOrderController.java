package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.OrderDAO;
import hu.db.techshop.dao.OrderStatusDAO;
import hu.db.techshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminOrderController {

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderStatusDAO orderStatusDAO;

    @GetMapping(value = "/admin/orders")
    public String newOrders(@RequestParam(name = "s", required = false) String s,
                            @RequestParam(name = "q", required = false) String q, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        int statusId = 1;
        if (s != null && Integer.parseInt(s) > 1 && Integer.parseInt(s) < 6) statusId = Integer.parseInt(s);

        if (q != null && !q.equals("")) {
            model.addAttribute("orderList", orderDAO.findAll(statusId, q));
        }
        else {
            model.addAttribute("orderList", orderDAO.findAll(statusId));
        }

        model.addAttribute("statuses", orderStatusDAO.findAll());

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

    @GetMapping(value = "/admin/orders/show/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView addComment(@PathVariable int id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return null;
        }

        model.addAttribute("order", orderDAO.findById(id));

        return new ModelAndView("/admin/orders/order :: order");
    }

    @PostMapping(value = "/admin/orders/status/{id}")
    public String status(@PathVariable int id, @RequestParam("statusId") String statusId, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        orderDAO.setStatus(id, Integer.parseInt(statusId));

        return "redirect:/admin/orders?s=" + statusId;
    }

}
