package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.InvoiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminInvoiceController {

    @Autowired
    InvoiceDAO invoiceDAO;

    @GetMapping(value = "/admin/invoices")
    public String index(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("invoiceList", invoiceDAO.findAll());

        return "admin/invoices/list";
    }

}
