package hu.db.techshop.dao;

import hu.db.techshop.model.Product;
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
public class ProductDAOImpl extends JdbcDaoSupport implements ProductDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Product> findAll(String sort) {
        String query = "SELECT P.*, C.SLUG AS CATEGORYSLUG " +
                       "FROM TS_PRODUCT P, TS_PRODUCT_CATEGORY C " +
                       "WHERE P.CATEGORYID=C.ID AND P.ACTIVE=1 " +
                       "ORDER BY " + (sort != null && sort.equals("price") ? "price" : "productname");
        return jdbcTemplate.query(query, (row, i) -> productMapper(row, false));
    }

    @Override
    public List<Product> findAll(String sort, int categoryId) {
        String query = "SELECT P.*, C.SLUG AS CATEGORYSLUG " +
                       "FROM TS_PRODUCT P, TS_PRODUCT_CATEGORY C " +
                       "WHERE P.CATEGORYID=C.ID AND P.ACTIVE=1 AND P.CATEGORYID=? " +
                       "ORDER BY " + (sort != null && sort.equals("price") ? "price" : "productname");
        return jdbcTemplate.query(query, preparedStatement -> {
                    preparedStatement.setInt(1, categoryId);
                }, (row, i) -> productMapper(row, false));
    }

    @Override
    public List<Product> findAll(String sort, String keyword) {
        String query = "SELECT P.*, C.SLUG AS CATEGORYSLUG " +
                       "FROM TS_PRODUCT P, TS_PRODUCT_CATEGORY C " +
                       "WHERE P.CATEGORYID=C.ID AND P.ACTIVE=1 AND LOWER(P.PRODUCTNAME) LIKE ? " +
                       "ORDER BY " + (sort != null && sort.equals("price") ? "price" : "productname");
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setString(1, "%"+ keyword.toLowerCase() +"%");
        }, (row, i) -> productMapper(row, false));
    }

    @Override
    public Product findById(int id) {
        try {
            String query = "SELECT * FROM TS_PRODUCT WHERE ACTIVE=1 AND ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Product product = productMapper(result, true);
                statement.close();

                return product;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Product findBySlug(String slug) {
        try {
            String query = "SELECT * FROM TS_PRODUCT WHERE ACTIVE=1 AND SLUG=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, slug);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Product product = productMapper(result, true);
                statement.close();

                return product;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Product save(Product product) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(
                    "UPDATE TS_PRODUCT SET PRODUCTNAME=?, PRODUCTNUMBER=? WHERE ID=?");
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getProductNumber());
            statement.setInt(3, product.getId());

            statement.executeUpdate();
            statement.close();

            return product;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(Product product) {

    }

    private Product productMapper(ResultSet result, boolean entity) throws SQLException {
        Product product = new Product();
        product.setId(result.getInt("id"));
        product.setProductName(result.getString("productname"));
        product.setSlug(result.getString("slug"));
        product.setPrice(result.getInt("price"));
        product.setImage(result.getString("image"));

        if (entity) {
            product.setProductNumber(result.getString("productnumber"));
            product.setShortDescription(result.getString("shortdescription"));
            product.setDescription(result.getString("description"));
            product.setVat(result.getDouble("vat"));
        }
        else {
            product.setCategorySlug(result.getString("categoryslug"));
        }

        return product;
    }

}
