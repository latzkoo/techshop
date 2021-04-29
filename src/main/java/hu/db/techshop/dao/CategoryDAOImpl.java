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
        String query = "SELECT * FROM TS_PRODUCT_CATEGORY ORDER BY CATEGORYNAME";
        return jdbcTemplate.query(query, (row, i) -> categoryMapper(row));
    }

    @Override
    public Category findById(int id) {
        try {
            String query = "SELECT * FROM TS_PRODUCT_CATEGORY WHERE ID=?";
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
            String query = "SELECT * FROM TS_PRODUCT_CATEGORY WHERE SLUG=?";
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
                        "VALUES (TS_PRODUCT_CATEGORY_SEQ.nextval,?,?,?,?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
                    statement.setString(1, category.getCategoryName());
                    statement.setString(2, category.getSlug());

                    if (category.isActive()) statement.setInt(4, 1);
                    else statement.setNull(4, Types.INTEGER);

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
    public void delete(Category category) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "DELETE FROM TS_PRODUCT_CATEGORY WHERE ID=?");

            statement.setInt(1, category.getId());
            statement.executeUpdate();

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Category categoryMapper(ResultSet result) throws SQLException {
        Category category = new Category();
        category.setId(result.getInt("id"));
        category.setCategoryName(result.getString("categoryname"));
        category.setSlug(result.getString("slug"));
        category.setActive(result.getBoolean("active"));
        category.setCreatedAt(result.getTimestamp("createdat"));

        return category;
    }

}
