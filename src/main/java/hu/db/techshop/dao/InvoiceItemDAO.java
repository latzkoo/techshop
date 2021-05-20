package hu.db.techshop.dao;

import hu.db.techshop.model.InvoiceItem;
import java.util.List;

public interface InvoiceItemDAO {

    List<InvoiceItem> findAll(int invoiceId);

}
