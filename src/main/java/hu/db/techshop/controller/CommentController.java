package hu.db.techshop.controller;

import hu.db.techshop.dao.CommentDAO;
import hu.db.techshop.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    CommentDAO commentDAO;

    @PostMapping(value = "/hozzaszolas/add/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView addComment(@PathVariable int id, @RequestParam String comment,
                                   Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return null;
        }

        commentDAO.save(new Comment((int) request.getSession().getAttribute("USERID"), id, comment));
        model.addAttribute("comments", commentDAO.findAll(id));

        return new ModelAndView("/fragments/comments :: comments");
    }

    @PostMapping(value = "/hozzaszolas/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView deleteComment(@PathVariable int id, @RequestParam("productId") int productId,
                                      Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("USERID") == null) {
            return null;
        }

        Comment comment = commentDAO.findById(id);

        if (comment.getUserId() != (int) request.getSession().getAttribute("USERID")) {
            return null;
        }

        commentDAO.delete(comment);
        model.addAttribute("comments", commentDAO.findAll(productId));

        return new ModelAndView("/fragments/comments :: comments");
    }

}
