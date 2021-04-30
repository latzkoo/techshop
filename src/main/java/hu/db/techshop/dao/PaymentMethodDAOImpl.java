package hu.db.techshop.dao;

import hu.db.techshop.model.PaymentMethod;
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
import java.util.List;

@Repository
public class PaymentMethodDAOImpl extends JdbcDaoSupport implements PaymentMethodDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<PaymentMethod> findAll() {
        String query = "SELECT * FROM TS_PAYMENT_METHOD ORDER BY PAYMENTMETHOD";
        return jdbcTemplate.query(query, (row, i) -> paymentMethodMapper(row));
    }

    @Override
    public PaymentMethod findById(int id) {
        try {
            String query = "SELECT * FROM TS_PAYMENT_METHOD WHERE ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                PaymentMethod paymentMethod = paymentMethodMapper(result);
                statement.close();

                return paymentMethod;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PaymentMethod save(PaymentMethod paymentMethod) {
        try {
            // Insert
            if (paymentMethod.getId() <= 0) {
                final String SQL = "INSERT INTO TS_PAYMENT_METHOD (ID, PAYMENTMETHOD) " +
                        "VALUES (TS_PRODUCT_CATEGORY_SEQ.nextval,?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
                    statement.setString(1, paymentMethod.getPaymentMethod());

                    return statement;
                }, keyHolder);

                if (keyHolder.getKey() != null) {
                    paymentMethod.setId(keyHolder.getKey().intValue());
                }
            }
            // Update
            else {
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE TS_PAYMENT_METHOD SET PAYMENTMETHOD=? WHERE ID=?");

                statement.setString(1, paymentMethod.getPaymentMethod());
                statement.setInt(2, paymentMethod.getId());

                statement.executeUpdate();
                statement.close();
            }

            return paymentMethod;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(PaymentMethod paymentMethod) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "DELETE FROM TS_PAYMENT_METHOD WHERE ID=?");

            statement.setInt(1, paymentMethod.getId());
            statement.executeUpdate();

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PaymentMethod paymentMethodMapper(ResultSet result) throws SQLException {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(result.getInt("id"));
        paymentMethod.setPaymentMethod(result.getString("paymentmethod"));

        return paymentMethod;
    }

}
