package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.InvoiceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping(value = "/admin/invoices/show/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView show(@PathVariable int id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return null;
        }

        model.addAttribute("invoice", invoiceDAO.findById(id));

        return new ModelAndView("/admin/invoices/invoice :: invoice");
    }

}
