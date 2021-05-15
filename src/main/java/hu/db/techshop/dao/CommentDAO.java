package hu.db.techshop.dao;

import hu.db.techshop.model.Comment;

import java.util.List;

public interface CommentDAO {

    List<Comment> findAll(int productId);
    Comment findById(int id);
    Comment save(Comment comment);
    void delete(Comment comment);

}
