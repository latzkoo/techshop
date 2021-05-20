package hu.db.techshop.dao;

import hu.db.techshop.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InvoiceDAOImpl extends JdbcDaoSupport implements InvoiceDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    InvoiceItemDAO invoiceItemDAO;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Invoice> findAll() {
        String query = "SELECT I.*, M.PAYMENTMETHOD, " +
                    "(SELECT COUNT(II.ID) FROM TS_INVOICE_ITEM II WHERE II.INVOICEID=I.ID) AS COUNT, " +
                    "(SELECT SUM(II.PRICE * II.QTY) FROM TS_INVOICE_ITEM II WHERE II.INVOICEID=I.ID) AS VALUE " +
                "FROM TS_INVOICE I " +
                "LEFT JOIN TS_PAYMENT_METHOD M " +
                "ON I.PAYMENTMETHODID=M.ID " +
                "ORDER BY I.ID DESC";
        return jdbcTemplate.query(query, (row, i) -> invoiceMapper(row, false));
    }

    @Override
    public Invoice findById(int id) {
        try {
            String query = "SELECT I.*, M.PAYMENTMETHOD, " +
                    "(SELECT COUNT(II.ID) FROM TS_INVOICE_ITEM II WHERE II.INVOICEID=I.ID) AS COUNT, " +
                    "(SELECT SUM(II.PRICE * II.QTY) FROM TS_INVOICE_ITEM II WHERE II.INVOICEID=I.ID) AS VALUE " +
                    "FROM TS_INVOICE I " +
                    "LEFT JOIN TS_PAYMENT_METHOD M " +
                    "ON I.PAYMENTMETHODID=M.ID " +
                    "WHERE I.ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Invoice invoice = invoiceMapper(result, true);
                statement.close();

                return invoice;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(int invoiceId) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM TS_INVOICE WHERE ID=?");

            statement.setInt(1, invoiceId);
            statement.executeUpdate();

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Invoice invoiceMapper(ResultSet result, boolean entity) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(result.getInt("id"));
        invoice.setOrderId(result.getInt("orderid"));
        invoice.setInvoiceDate(result.getTimestamp("invoicedate"));
        invoice.setDeliveryDate(result.getTimestamp("deliverydate"));
        invoice.setDueDate(result.getTimestamp("duedate"));
        invoice.setPaymentMethodId(result.getInt("paymentmethodid"));
        invoice.setCustomerName(result.getString("customername"));
        invoice.setItemCount(result.getInt("count"));
        invoice.setInvoiceValue(result.getInt("value"));
        invoice.setPaymentMethod(result.getString("paymentmethod"));

        if (entity) {
            invoice.setSupplierName(result.getString("suppliername"));
            invoice.setSupplierZip(result.getInt("supplierzip"));
            invoice.setSupplierCity(result.getString("suppliercity"));
            invoice.setSupplierStreet(result.getString("supplierstreet"));
            invoice.setSupplierVat(result.getString("suppliervat"));
            invoice.setSupplierBankAccount(result.getString("supplierbankaccount"));
            invoice.setSupplierPhone(result.getString("supplierphone"));
            invoice.setCustomerZip(result.getInt("customerzip"));
            invoice.setCustomerCity(result.getString("customercity"));
            invoice.setCustomerStreet(result.getString("customerstreet"));
            invoice.setCustomerVat(result.getString("customervat"));
            invoice.setInvoiceComment(result.getString("invoicecomment"));
            invoice.setOriginalInvoiceId(result.getInt("originalinvoiceid"));
            invoice.setItems(invoiceItemDAO.findAll(invoice.getId()));
        }

        return invoice;
    }

}
