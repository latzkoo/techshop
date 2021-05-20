package hu.db.techshop.dao;

import hu.db.techshop.model.InvoiceItem;
import hu.db.techshop.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceItemDAOImpl extends JdbcDaoSupport implements InvoiceItemDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<InvoiceItem> findAll(int invoiceId) {
        String query = "SELECT * " +
                "FROM TS_INVOICE_ITEM " +
                "WHERE INVOICEID=? " +
                "ORDER BY ID";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, invoiceId);
        }, (row, i) -> invoiceItemMapper(row));
    }

    private InvoiceItem invoiceItemMapper(ResultSet result) throws SQLException {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setId(result.getInt("id"));
        invoiceItem.setInvoiceId(result.getInt("invoiceid"));
        invoiceItem.setProductNumber(result.getString("productnumber"));
        invoiceItem.setProductName(result.getString("productname"));
        invoiceItem.setPrice(result.getInt("price"));
        invoiceItem.setVat(result.getDouble("vat"));
        invoiceItem.setQty(result.getInt("qty"));

        return invoiceItem;
    }

}
