package hu.db.techshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CassaController {

    @GetMapping(value = "/penztar")
    public String index(Model model, HttpServletRequest request) {
        return "cassa";
    }

}
