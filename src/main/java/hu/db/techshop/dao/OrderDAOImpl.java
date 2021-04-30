package hu.db.techshop.dao;

import hu.db.techshop.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class OrderDAOImpl extends JdbcDaoSupport implements OrderDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Order> findAll() {
        String query = "SELECT * FROM TS_ORDER ORDER BY CREATEADAT DESC";
        return jdbcTemplate.query(query, (row, i) -> orderMapper(row));
    }

    @Override
    public Order findById(int id) {
        try {
            String query = "SELECT * FROM TS_ORDER WHERE ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Order orderStatus = orderMapper(result);
                statement.close();

                return orderStatus;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Order save(Order order, int userId) {
        try {
            // Insert
            if (order.getId() <= 0) {
                final String SQL = "INSERT INTO TS_ORDER (ID, USERID, PAYMENTMETHODID, SHIPNAME, SHIPZIP, SHIPCITY, SHIPSTREET, " +
                        "SHIPPHONE, BILLNAME, BILLZIP, BILLCITY, BILLSTREET, BILLTAXNUM) " +
                        "VALUES (TS_ORDER_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
                    statement.setInt(1, userId);
                    statement.setInt(2, order.getPaymentMethodId());
                    statement.setString(3, order.getShippingName());
                    statement.setInt(4, order.getShippingZip());
                    statement.setString(5, order.getShippingCity());
                    statement.setString(6, order.getShippingStreet());
                    statement.setString(7, order.getShippingPhone());
                    statement.setString(8, order.getBillingName());
                    statement.setInt(9, order.getBillingZip());
                    statement.setString(10, order.getBillingCity());
                    statement.setString(11, order.getBillingStreet());
                    statement.setString(12, order.getBillingTaxNumber());

                    return statement;
                }, keyHolder);

                if (keyHolder.getKey() != null) {
                    order.setId(keyHolder.getKey().intValue());
                }
            }
            // Update
            else {
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE TS_ORDER SET PAYMENTMETHODID=?, SHIPNAME=?, SHIPZIP=?, SHIPCITY=?, SHIPSTREET=?, " +
                                "SHIPPHONE=?, BILLNAME=?, BILLZIP=?, BILLCITY=?, BILLSTREET=?, BILLTAXNUM=? WHERE ID=?");

                statement.setInt(1, order.getPaymentMethodId());
                statement.setString(2, order.getShippingName());
                statement.setInt(3, order.getShippingZip());
                statement.setString(4, order.getShippingCity());
                statement.setString(5, order.getShippingStreet());
                statement.setString(6, order.getShippingPhone());
                statement.setString(7, order.getBillingName());
                statement.setInt(8, order.getBillingZip());
                statement.setString(9, order.getBillingCity());
                statement.setString(10, order.getBillingStreet());
                statement.setString(11, order.getBillingTaxNumber());
                statement.setInt(12, order.getId());

                statement.executeUpdate();
                statement.close();
            }

            return order;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(Order order) {

    }

    private Order orderMapper(ResultSet result) throws SQLException {
        Order order = new Order();
        order.setId(result.getInt("id"));
        order.setPaymentMethodId(result.getInt("paymentmethodid"));
        order.setCreatedAt(result.getTimestamp("createdat"));

        return order;
    }

}
