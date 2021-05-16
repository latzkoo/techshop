package hu.db.techshop.dao;

import hu.db.techshop.model.Category;
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
public class CategoryDAOImpl extends JdbcDaoSupport implements CategoryDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Category> findAll() {
        String query = "SELECT C.*, " +
                "(SELECT COUNT(P.ID) FROM TS_PRODUCT P WHERE P.CATEGORYID = C.ID) AS COUNT " +
                "FROM TS_PRODUCT_CATEGORY C " +
                "ORDER BY C.CATEGORYNAME";
        return jdbcTemplate.query(query, (row, i) -> categoryMapper(row));
    }

    @Override
    public List<Category> findAll(int status) {
        String query = "SELECT C.*, " +
                    "(SELECT COUNT(P.ID) FROM TS_PRODUCT P WHERE P.CATEGORYID = C.ID) AS COUNT " +
                "FROM TS_PRODUCT_CATEGORY C " +
                "WHERE C.ACTIVE = ? " +
                "ORDER BY C.CATEGORYNAME";
        return jdbcTemplate.query(query, preparedStatement -> {
            if (status > 0) preparedStatement.setInt(1, 1);
            else preparedStatement.setNull(1, Types.INTEGER);
        }, (row, i) -> categoryMapper(row));
    }

    @Override
    public List<Category> findAll(String keyword) {
        String query = "SELECT C.*, " +
                "(SELECT COUNT(P.ID) FROM TS_PRODUCT P WHERE P.CATEGORYID = C.ID) AS COUNT " +
                "FROM TS_PRODUCT_CATEGORY C " +
                "WHERE LOWER(CATEGORYNAME) LIKE ? " +
                "ORDER BY C.CATEGORYNAME";

        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setString(1, "%"+ keyword.toLowerCase() +"%");
        }, (row, i) -> categoryMapper(row));
    }

    @Override
    public List<Category> findAll(int status, String keyword) {
        String query = "SELECT C.*, " +
                "(SELECT COUNT(P.ID) FROM TS_PRODUCT P WHERE P.CATEGORYID = C.ID) AS COUNT " +
                "FROM TS_PRODUCT_CATEGORY C " +
                "WHERE C.ACTIVE = ? AND LOWER(CATEGORYNAME) LIKE ? " +
                "ORDER BY C.CATEGORYNAME";

        return jdbcTemplate.query(query, preparedStatement -> {
            if (status > 0) preparedStatement.setInt(1, 1);
            else preparedStatement.setNull(1, Types.INTEGER);

            preparedStatement.setString(2, "%"+ keyword.toLowerCase() +"%");
        }, (row, i) -> categoryMapper(row));
    }

    @Override
    public Category findById(int id) {
        try {
            String query = "SELECT C.*, (SELECT COUNT(P.ID) FROM TS_PRODUCT P WHERE P.CATEGORYID = C.ID) AS COUNT " +
                    "FROM TS_PRODUCT_CATEGORY C WHERE C.ACTIVE=1 AND C.ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Category category = categoryMapper(result);
                statement.close();

                return category;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Category findBySlug(String slug) {
        try {
            String query = "SELECT C.*, (SELECT COUNT(P.ID) FROM TS_PRODUCT P WHERE P.CATEGORYID = C.ID) AS COUNT " +
                    "FROM TS_PRODUCT_CATEGORY C WHERE C.ACTIVE=1 AND C.SLUG=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, slug);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Category category = categoryMapper(result);
                statement.close();

                return category;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Category save(Category category) {
        try {
            // Insert
            if (category.getId() <= 0) {
                final String SQL = "INSERT INTO TS_PRODUCT_CATEGORY (ID, CATEGORYNAME, SLUG, ACTIVE) " +
                        "VALUES (TS_PRODUCT_CATEGORY_SEQ.nextval,?,?,?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
                    statement.setString(1, category.getCategoryName());
                    statement.setString(2, category.getSlug());
                    statement.setInt(3, 1);

                    return statement;
                }, keyHolder);

                if (keyHolder.getKey() != null) {
                    category.setId(keyHolder.getKey().intValue());
                }
            }
            // Update
            else {
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE TS_PRODUCT_CATEGORY SET CATEGORYNAME=?, SLUG=? WHERE ID=?");

                statement.setString(1, category.getCategoryName());
                statement.setString(2, category.getSlug());
                statement.setInt(3, category.getId());

                statement.executeUpdate();
                statement.close();
            }

            return category;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int delete(Category category) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "DELETE FROM TS_PRODUCT_CATEGORY WHERE ID=?");

            statement.setInt(1, category.getId());
            int deleted = statement.executeUpdate();
            statement.close();

            return deleted;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Category categoryMapper(ResultSet result) throws SQLException {
        Category category = new Category();
        category.setId(result.getInt("id"));
        category.setCategoryName(result.getString("categoryname"));
        category.setSlug(result.getString("slug"));
        category.setActive(result.getBoolean("active"));
        category.setCreatedAt(result.getTimestamp("createdat"));
        category.setProductCount(result.getInt("count"));

        return category;
    }

}
