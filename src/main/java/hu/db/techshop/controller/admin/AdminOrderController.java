package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.OrderDAO;
import hu.db.techshop.dao.OrderStatusDAO;
import hu.db.techshop.dao.PaymentMethodDAO;
import hu.db.techshop.model.Order;
import hu.db.techshop.model.Product;
import hu.db.techshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AdminOrderController {

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderStatusDAO orderStatusDAO;

    @Autowired
    PaymentMethodDAO paymentMethodDAO;

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
    public ModelAndView show(@PathVariable int id, Model model, HttpServletRequest request) {
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

    @GetMapping(value = "/admin/orders/edit/{id}")
    public String edit(@PathVariable int id, Order order, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("paymentMethods", paymentMethodDAO.findAll());
        model.addAttribute("order", orderDAO.findById(id));

        return "admin/orders/form";
    }

    @PostMapping(value = "/admin/orders")
    public String doCheckout(@Valid Order order, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("paymentMethods", paymentMethodDAO.findAll());
            return "admin/orders/form";
        }

        orderDAO.save(order, (int) request.getSession().getAttribute("USERID"));

        return "redirect:/admin/orders?success=modify";
    }

}
