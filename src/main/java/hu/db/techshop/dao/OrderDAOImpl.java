package hu.db.techshop.dao;

import hu.db.techshop.model.Order;
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
public class OrderDAOImpl extends JdbcDaoSupport implements OrderDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    OrderItemDAO orderItemDAO;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Order> findAll(int statusId) {
        String query = "SELECT O.*, U.FIRSTNAME, U.LASTNAME, M.PAYMENTMETHOD, S.STATUS, " +
                "(SELECT COUNT(I.ID) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS COUNT, " +
                "(SELECT SUM(I.PRICE * I.QTY) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS VALUE " +
                "FROM TS_ORDER O " +
                "LEFT JOIN TS_USER U " +
                "ON O.USERID=U.ID " +
                "LEFT JOIN TS_PAYMENT_METHOD M " +
                "ON O.PAYMENTMETHODID=M.ID " +
                "LEFT JOIN TS_ORDER_STATUS S " +
                "ON O.STATUSID=S.ID " +
                "WHERE O.STATUSID=? " +
                "ORDER BY O.CREATEDAT DESC";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, statusId);
        }, (row, i) -> orderMapper(row, false, false));
    }

    @Override
    public List<Order> findAll(int statusId, String keyword) {
        String query = "SELECT O.*, U.FIRSTNAME, U.LASTNAME, M.PAYMENTMETHOD, S.STATUS, " +
                "(SELECT COUNT(I.ID) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS COUNT, " +
                "(SELECT SUM(I.PRICE * I.QTY) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS VALUE " +
                "FROM TS_ORDER O " +
                "LEFT JOIN TS_USER U " +
                "ON O.USERID=U.ID " +
                "LEFT JOIN TS_PAYMENT_METHOD M " +
                "ON O.PAYMENTMETHODID=M.ID " +
                "LEFT JOIN TS_ORDER_STATUS S " +
                "ON O.STATUSID=S.ID " +
                "WHERE O.STATUSID=? " +
                "AND (LOWER(U.FIRSTNAME) LIKE ? OR LOWER(U.LASTNAME) LIKE ? OR U.EMAIL LIKE ? " +
                "OR (O.ID IN(" +
                    "SELECT OI.ORDERID " +
                    "FROM TS_ORDER_ITEM OI, TS_PRODUCT P " +
                    "WHERE OI.PRODUCTID=P.ID " +
                    "AND LOWER(P.PRODUCTNAME) LIKE ?) OR M.PAYMENTMETHOD LIKE ?))" +
                "ORDER BY O.CREATEDAT DESC";

        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, statusId);
            preparedStatement.setString(2, "%"+ keyword.toLowerCase() +"%");
            preparedStatement.setString(3, "%"+ keyword.toLowerCase() +"%");
            preparedStatement.setString(4, "%"+ keyword.toLowerCase() +"%");
            preparedStatement.setString(5, "%"+ keyword.toLowerCase() +"%");
            preparedStatement.setString(6, "%"+ keyword.toLowerCase() +"%");
        }, (row, i) -> orderMapper(row, false, false));
    }

    @Override
    public List<Order> findAllByUserId(int id) {
        String query = "SELECT O.*, U.FIRSTNAME, U.LASTNAME, M.PAYMENTMETHOD, S.STATUS, " +
                "(SELECT SUM(I.QTY) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS COUNT, " +
                "(SELECT SUM(I.PRICE * I.QTY) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS VALUE " +
                "FROM TS_ORDER O " +
                "LEFT JOIN TS_USER U " +
                "ON O.USERID=U.ID " +
                "LEFT JOIN TS_PAYMENT_METHOD M " +
                "ON O.PAYMENTMETHODID=M.ID " +
                "LEFT JOIN TS_ORDER_STATUS S " +
                "ON O.STATUSID=S.ID " +
                "WHERE O.USERID=? " +
                "ORDER BY O.CREATEDAT DESC";

        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, id);
        }, (row, i) -> orderMapper(row, false, true));
    }

    @Override
    public Order findById(int id) {
        try {
            String query = "SELECT O.*, U.FIRSTNAME, U.LASTNAME, M.PAYMENTMETHOD, S.STATUS, " +
                    "(SELECT COUNT(I.ID) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS COUNT, " +
                    "(SELECT SUM(I.PRICE * I.QTY) FROM TS_ORDER_ITEM I WHERE I.ORDERID=O.ID) AS VALUE " +
                    "FROM TS_ORDER O " +
                    "LEFT JOIN TS_PAYMENT_METHOD M " +
                    "ON O.PAYMENTMETHODID=M.ID " +
                    "LEFT JOIN TS_USER U " +
                    "ON O.USERID=U.ID " +
                    "LEFT JOIN TS_ORDER_STATUS S " +
                    "ON O.STATUSID=S.ID " +
                    "WHERE O.ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Order order = orderMapper(result, true, true);
                order.setItems(orderItemDAO.findAll(id));
                statement.close();

                return order;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @Transactional
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
    public void setStatus(int id, int statusId) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE TS_ORDER SET STATUSID=? WHERE ID=?");
            statement.setInt(1, statusId);
            statement.setInt(2, id);

            statement.executeUpdate();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int orderId) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM TS_ORDER WHERE ID=?");

            statement.setInt(1, orderId);
            statement.executeUpdate();

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order orderMapper(ResultSet result, boolean entity, boolean withItems) throws SQLException {
        Order order = new Order();
        order.setId(result.getInt("id"));
        order.setPaymentMethodId(result.getInt("paymentmethodid"));
        order.setCreatedAt(result.getTimestamp("createdat"));
        order.setPaymentMethod(result.getString("paymentmethod"));
        order.setStatusId(result.getInt("statusid"));
        order.setStatus(result.getString("status"));
        order.setProductCount(result.getInt("count"));
        order.setOrderValue(result.getInt("value"));

        if (entity) {
            order.setShippingName(result.getString("shipname"));
            order.setShippingZip(result.getInt("shipzip"));
            order.setShippingCity(result.getString("shipcity"));
            order.setShippingStreet(result.getString("shipstreet"));
            order.setShippingPhone(result.getString("shipphone"));
            order.setBillingName(result.getString("billname"));
            order.setBillingZip(result.getInt("billzip"));
            order.setBillingCity(result.getString("billcity"));
            order.setBillingStreet(result.getString("billstreet"));
            order.setBillingTaxNumber(result.getString("billtaxnum"));
        }
        else {
            order.setFirstname(result.getString("firstname"));
            order.setLastname(result.getString("lastname"));
        }

        if (withItems) {
            order.setItems(orderItemDAO.findAll(order.getId()));
        }

        return order;
    }

}
