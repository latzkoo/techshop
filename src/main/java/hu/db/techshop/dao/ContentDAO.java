package hu.db.techshop.dao;

import hu.db.techshop.model.Content;

import java.util.List;

public interface ContentDAO {

    List<Content> findAll();
    List<Content> findAll(String keyword);
    Content findById(int id);
    Content save(Content content);

}
