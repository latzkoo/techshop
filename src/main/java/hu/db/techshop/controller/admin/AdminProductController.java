package hu.db.techshop.controller.admin;

import hu.db.techshop.dao.CategoryDAO;
import hu.db.techshop.dao.ProductDAO;
import hu.db.techshop.model.Product;
import hu.db.techshop.util.Slug;
import hu.db.techshop.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Controller
public class AdminProductController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ProductDAO productDAO;

    @GetMapping(value = "/admin/products")
    public String index(@RequestParam(name = "q", required = false) String q, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        if (q != null && !q.equals("")) {
            model.addAttribute("productList", productDAO.findAll("PRODUCTNAME", q, false));
        }
        else {
            model.addAttribute("productList", productDAO.findAll("PRODUCTNAME", false));
        }

        return "admin/products/list";
    }

    @GetMapping(value = "/admin/products/create")
    public String create(Product product, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("categories", categoryDAO.findAll());

        return "admin/products/form";
    }

    @GetMapping(value = "/admin/products/edit/{id}")
    public String edit(@PathVariable int id, Product product, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("categories", categoryDAO.findAll());
        model.addAttribute("product", productDAO.findById(id, false));

        return "admin/products/form";
    }

    @GetMapping(value = "/admin/products/delete/{id}")
    public String edit(@PathVariable int id, HttpServletRequest request) {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        Product product = productDAO.findById(id, false);

        if (product.getImage() != null) {
            Util.deleteFile("uploads/products/large/" + product.getImage());
            Util.deleteFile("uploads/products/thumbs/" + product.getImage());
        }

        if (productDAO.delete(product) > 0) {
            return "redirect:/admin/products?success=delete";
        }

        return "redirect:/admin/products?error=delete";
    }

    @PostMapping(value = "/admin/products")
    public String insert(@Valid Product product, @RequestParam("file") MultipartFile file,
                         BindingResult bindingResult, Model model, HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("ADMIN") == null) {
            return "redirect:/admin/login";
        }

        Product oldProduct = null;

        if (product.getId() > 0) {
            oldProduct = productDAO.findById(product.getId(), false);
            product.setImage(oldProduct.getImage());
        }

        if (file.getSize() > 0) {
            if (oldProduct != null && oldProduct.getImage() != null) {
                Util.deleteFile("uploads/products/large/" + product.getImage());
                Util.deleteFile("uploads/products/thumbs/" + product.getImage());
            }

            String imageName = StringUtils.cleanPath(file.getOriginalFilename());
            Util.saveFile("uploads/products/large", imageName, file);
            Util.createThumbnail("uploads/products/large/" + imageName,
                    "uploads/products/thumbs/" + imageName, 150);

            product.setImage(imageName);
        }

        product.setSlug(Slug.from(product.getProductName()));
        productDAO.save(product);

        return "redirect:/admin/products?success=" + (product.getId() > 0 ? "modify" : "add");
    }

}
