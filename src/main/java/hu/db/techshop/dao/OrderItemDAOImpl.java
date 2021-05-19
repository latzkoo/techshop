package hu.db.techshop.dao;

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
public class OrderItemDAOImpl extends JdbcDaoSupport implements OrderItemDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<OrderItem> findAll(int orderId) {
        String query = "SELECT I.*, P.PRODUCTNUMBER, P.PRODUCTNAME " +
                "FROM TS_ORDER_ITEM I " +
                "LEFT JOIN TS_PRODUCT P " +
                "ON I.PRODUCTID=P.ID " +
                "WHERE I.ORDERID=? " +
                "ORDER BY I.ID";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, orderId);
        }, (row, i) -> orderItemMapper(row));
    }

    @Override
    public OrderItem findById(int id) {
        try {
            String query = "SELECT I.*, P.PRODUCTNUMBER, P.PRODUCTNAME " +
                    "FROM TS_ORDER_ITEM I " +
                    "LEFT JOIN TS_PRODUCT P " +
                    "ON I.PRODUCTID=P.ID " +
                    "WHERE I.ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                OrderItem orderItem = orderItemMapper(result);
                statement.close();

                return orderItem;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveAll(int orderId, List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            orderItem.setId(insert(orderId, orderItem));
        }
    }

    @Override
    @Transactional
    public OrderItem save(int orderId, OrderItem orderItem) {
        try {
            // Insert
            if (orderItem.getId() <= 0) {
                orderItem.setId(insert(orderId, orderItem));
            }
            // Update
            else {
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE TS_ORDER_ITEM SET PRODUCTID=?, PRICE=?, QTY=? WHERE ID=?");

                statement.setInt(1, orderItem.getProductId());
                statement.setInt(2, orderItem.getPrice());
                statement.setInt(3, orderItem.getQty());
                statement.setInt(4, orderItem.getId());

                statement.executeUpdate();
                statement.close();
            }

            return orderItem;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int insert(int orderId, OrderItem orderItem) {
        final String SQL = "INSERT INTO TS_ORDER_ITEM (ID, ORDERID, PRODUCTID, PRICE, QTY) " +
                "VALUES (TS_ORDER_ITEM_SEQ.nextval,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
            statement.setInt(1, orderId);
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getPrice());
            statement.setInt(4, orderItem.getQty());

            return statement;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().intValue();
        }

        return 0;
    }

    @Override
    public void deleteAll(int orderId) {

    }

    @Override
    public void delete(OrderItem orderItem) {

    }

    private OrderItem orderItemMapper(ResultSet result) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(result.getInt("id"));
        orderItem.setOrderId(result.getInt("orderid"));
        orderItem.setProductId(result.getInt("productid"));
        orderItem.setProductNumber(result.getString("productnumber"));
        orderItem.setProductName(result.getString("productname"));
        orderItem.setPrice(result.getInt("price"));
        orderItem.setQty(result.getInt("qty"));

        return orderItem;
    }

}
