package hu.db.techshop.controller;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.PaymentMethodDAO;
import hu.db.techshop.model.Cart;
import hu.db.techshop.model.Order;
import hu.db.techshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class CheckoutController {

    @Autowired
    Cart cart;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    PaymentMethodDAO paymentMethodDAO;

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/kosar")
    public String cart(@RequestParam(name = "delete", required = false) String productId,
                       Model model, HttpServletRequest request) {
        if (productId != null) {
            cart.remove(Integer.parseInt(productId));
            return "redirect:/kosar";
        }

        model.addAttribute("categoryList", categoryDAO.findAll());

        return "cart";
    }

    @GetMapping(value = "/penztar")
    public String checkout(Model model, HttpServletRequest request, Order order) {
        if (request.getSession().getAttribute("USERID") == null) {
            return "redirect:/belepes";
        }

        model.addAttribute("categoryList", categoryDAO.findAll());
        model.addAttribute("paymentMethods", paymentMethodDAO.findAll());

        return "checkout";
    }

    @PostMapping(value = "/penztar")
    public String doCheckout(@Valid Order order, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return "redirect:/belepes";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("paymentMethods", paymentMethodDAO.findAll());
            return "checkout";
        }

        orderService.addOrder(order, cart, (int) request.getSession().getAttribute("USERID"));

        return "redirect:/koszonjuk";
    }

}
