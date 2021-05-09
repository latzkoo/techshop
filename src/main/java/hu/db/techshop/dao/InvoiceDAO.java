package hu.db.techshop.dao;

import hu.db.techshop.model.Invoice;

import java.util.List;

public interface InvoiceDAO {

    List<Invoice> findAll();
    Invoice findById(int id);
    void delete(int orderId);

}
