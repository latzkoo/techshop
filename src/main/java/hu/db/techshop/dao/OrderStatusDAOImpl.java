package hu.db.techshop.dao;

import hu.db.techshop.model.OrderStatus;
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
public class OrderStatusDAOImpl extends JdbcDaoSupport implements OrderStatusDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<OrderStatus> findAll() {
        String query = "SELECT * FROM TS_ORDER_STATUS ORDER BY ID";
        return jdbcTemplate.query(query, (row, i) -> orderStatusMapper(row));
    }

    @Override
    public OrderStatus findById(int id) {
        try {
            String query = "SELECT * FROM TS_ORDER_STATUS WHERE ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                OrderStatus orderStatus = orderStatusMapper(result);
                statement.close();

                return orderStatus;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private OrderStatus orderStatusMapper(ResultSet result) throws SQLException {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(result.getInt("id"));
        orderStatus.setStatus(result.getString("status"));

        return orderStatus;
    }

}
