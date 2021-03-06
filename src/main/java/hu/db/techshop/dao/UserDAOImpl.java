package hu.db.techshop.dao;

import hu.db.techshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class UserDAOImpl extends JdbcDaoSupport implements UserDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM ts_user";
        return jdbcTemplate.query(query, (row, i) -> userMapper(row));
    }

    @Override
    public List<User> findAll(String keyword) {
        String query = "SELECT * FROM TS_USER WHERE (LOWER(FIRSTNAME) LIKE ? OR LOWER(LASTNAME) LIKE ? OR LOWER(EMAIL) LIKE ?)" +
                "ORDER BY LASTNAME, FIRSTNAME";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setString(1, "%"+ keyword.toLowerCase() +"%");
            preparedStatement.setString(2, "%"+ keyword.toLowerCase() +"%");
            preparedStatement.setString(3, "%"+ keyword.toLowerCase() +"%");
        }, (row, i) -> userMapper(row));
    }

    @Override
    public User findById(int id) {
        try {
            String query = "SELECT * FROM ts_user WHERE id=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                User user = userMapper(result);
                statement.close();

                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User save(User user) {
        try {
            // Insert
            if (user.getId() <= 0) {
                final String SQL = "INSERT INTO TS_USER (ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, ADMIN) " +
                        "VALUES (TS_USER_SEQ.nextval,?,?,?,?,?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
                    statement.setString(1, user.getFirstname());
                    statement.setString(2, user.getLastname());
                    statement.setString(3, user.getEmail());
                    statement.setString(4, passwordEncoder.encode(user.getPassword()));

                    if (user.isAdmin()) statement.setInt(5, 1);
                    else statement.setNull(5, Types.INTEGER);

                    return statement;
                }, keyHolder);

                if (keyHolder.getKey() != null) {
                    user.setId(keyHolder.getKey().intValue());
                }
            }
            // Update
            else {
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE TS_USER SET FIRSTNAME=?, LASTNAME=?, EMAIL=?, ADMIN=? WHERE ID=?");

                statement.setString(1, user.getFirstname());
                statement.setString(2, user.getLastname());
                statement.setString(3, user.getEmail());
                statement.setBoolean(4, user.isAdmin());
                statement.setInt(5, user.getId());

                statement.executeUpdate();
                statement.close();
            }

            return user;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int delete(User user) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM TS_USER WHERE ID=?");

            statement.setInt(1, user.getId());
            int deleted = statement.executeUpdate();
            statement.close();

            return deleted;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private User userMapper(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getInt("id"));
        user.setFirstname(result.getString("firstname"));
        user.setLastname(result.getString("lastname"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
        user.setCreatedAt(result.getTimestamp("createdat"));
        user.setAdmin(result.getBoolean("admin"));
        user.setPoints(result.getInt("points"));

        return user;
    }

    public User getUserByEmailAndPassword(String email, String password, boolean checkAdmin) {
        try {
            String query = "SELECT * FROM TS_USER WHERE email=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                if (passwordEncoder.matches(password, result.getString("password"))) {
                    User user = userMapper(result);

                    if (checkAdmin) {
                        if (user.isAdmin()) {
                            return user;
                        }
                    }
                    else {
                        return user;
                    }
                }

                statement.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User updatePassword(User user) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE TS_USER SET PASSWORD=? WHERE ID=?");
            statement.setString(1, passwordEncoder.encode(user.getPassword()));
            statement.setInt(2, user.getId());

            statement.executeUpdate();
            statement.close();

            return user;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
