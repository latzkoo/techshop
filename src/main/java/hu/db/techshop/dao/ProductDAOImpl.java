package hu.db.techshop.dao;

import hu.db.techshop.model.Product;
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
public class ProductDAOImpl extends JdbcDaoSupport implements ProductDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CommentDAO commentDAO;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Product> findAll(String sort, boolean isActive) {
        String query = "SELECT P.ID, P.CATEGORYID, P.PRODUCTNAME, P.SLUG, P.PRICE, P.IMAGE, P.CREATEDAT, C.SLUG AS CATEGORYSLUG " +
                       "FROM TS_PRODUCT P, TS_PRODUCT_CATEGORY C " +
                       "WHERE P.CATEGORYID=C.ID " + (isActive ? "AND P.ACTIVE = 1 " : "") +
                       "ORDER BY " + (sort != null && sort.equals("price") ? "price" : "productname");
        return jdbcTemplate.query(query, (row, i) -> productMapper(row, false));
    }

    @Override
    public List<Product> findAll(String sort, int categoryId, boolean isActive) {
        String query = "SELECT P.ID, P.CATEGORYID, P.SLUG, P.PRODUCTNAME, P.PRICE, P.IMAGE, P.CREATEDAT, C.SLUG AS CATEGORYSLUG " +
                       "FROM TS_PRODUCT P, TS_PRODUCT_CATEGORY C " +
                       "WHERE P.CATEGORYID=C.ID AND P.CATEGORYID=? " + (isActive ? "AND P.ACTIVE = 1 " : "") +
                       "ORDER BY " + (sort != null && sort.equals("price") ? "price" : "productname");
        return jdbcTemplate.query(query, preparedStatement -> {
                    preparedStatement.setInt(1, categoryId);
                }, (row, i) -> productMapper(row, false));
    }

    @Override
    public List<Product> findAll(String sort, String keyword, boolean isActive) {
        String query = "SELECT P.ID, P.CATEGORYID, P.SLUG, P.PRODUCTNAME, P.PRICE, P.IMAGE, P.CREATEDAT, C.SLUG AS CATEGORYSLUG " +
                       "FROM TS_PRODUCT P, TS_PRODUCT_CATEGORY C " +
                       "WHERE P.CATEGORYID=C.ID AND LOWER(P.PRODUCTNAME) LIKE ? " + (isActive ? "AND P.ACTIVE = 1 " : "") +
                       "ORDER BY " + (sort != null && sort.equals("price") ? "price" : "productname");
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setString(1, "%"+ keyword.toLowerCase() +"%");
        }, (row, i) -> productMapper(row, false));
    }

    @Override
    public List<Product> findSimilar(Product product) {
        String query = "SELECT P.ID, P.CATEGORYID, P.SLUG, P.PRODUCTNAME, P.PRICE, P.IMAGE, P.CREATEDAT, C.SLUG AS CATEGORYSLUG " +
                "FROM TS_ORDER_ITEM I " +
                "LEFT JOIN TS_PRODUCT P ON I.PRODUCTID=P.ID " +
                "LEFT JOIN TS_PRODUCT_CATEGORY C ON P.CATEGORYID=C.ID " +
                "WHERE P.ACTIVE = 1 " +
                "AND I.ORDERID IN( SELECT OI.ORDERID FROM TS_ORDER_ITEM OI WHERE OI.PRODUCTID = ?) " +
                "GROUP BY P.ID, P.CATEGORYID, P.SLUG, P.PRODUCTNAME, P.PRICE, P.IMAGE, P.CREATEDAT, C.SLUG " +
                "ORDER BY DBMS_RANDOM.RANDOM() " +
                "FETCH FIRST 5 ROWS ONLY";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, product.getId());
        }, (row, i) -> productMapper(row, false));
    }

    @Override
    public List<Product> findFreshest(int categoryId) {
        String query = "SELECT P.ID, P.CATEGORYID, P.SLUG, P.PRODUCTNAME, P.PRICE, P.IMAGE, P.CREATEDAT, C.SLUG AS CATEGORYSLUG " +
                "FROM TS_PRODUCT P, TS_PRODUCT_CATEGORY C " +
                "WHERE P.CATEGORYID=C.ID AND P.ACTIVE = 1 " +
                "AND P.CREATEDAT > TRUNC(SYSDATE) - INTERVAL '30' DAY " +
                "AND P.CATEGORYID = ?" +
                "ORDER BY DBMS_RANDOM.RANDOM() " +
                "FETCH FIRST 5 ROWS ONLY";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, categoryId);
        }, (row, i) -> productMapper(row, false));
    }

    @Override
    public Product findById(int id, boolean isActive) {
        try {
            String query = "SELECT * FROM TS_PRODUCT WHERE ID=?" + (isActive ? " AND ACTIVE = 1 " : "");
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
    public Product findBySlug(String slug, boolean isActive) {
        try {
            String query = "SELECT * FROM TS_PRODUCT WHERE SLUG=?" + (isActive ? " AND ACTIVE = 1 " : "");
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, slug);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Product product = productMapper(result, true);
                product.setComments(commentDAO.findAll(product.getId()));

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
            // Insert
            if (product.getId() <= 0) {
                final String SQL = "INSERT INTO TS_PRODUCT (ID, CATEGORYID, PRODUCTNUMBER, SLUG, PRODUCTNAME, " +
                        "SHORTDESCRIPTION, DESCRIPTION, PRICE, VAT, IMAGE, ACTIVE) " +
                        "VALUES (TS_PRODUCT_SEQ.nextval,?,?,?,?,?,?,?,?,?,?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
                    statement.setInt(1, product.getCategoryId());
                    statement.setString(2, product.getProductNumber());
                    statement.setString(3, product.getSlug());
                    statement.setString(4, product.getProductName());
                    statement.setString(5, product.getShortDescription());
                    statement.setString(6, product.getDescription());
                    statement.setInt(7, product.getPrice());
                    statement.setDouble(8, product.getVat());
                    statement.setString(9, product.getImage());
                    statement.setInt(10, 1);

                    return statement;
                }, keyHolder);

                if (keyHolder.getKey() != null) {
                    product.setId(keyHolder.getKey().intValue());
                }
            }
            // Update
            else {
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE TS_PRODUCT SET CATEGORYID=?, PRODUCTNUMBER=?, SLUG=?, PRODUCTNAME=?, " +
                                "SHORTDESCRIPTION=?, DESCRIPTION=?, PRICE=?, VAT=?, IMAGE=? WHERE ID=?");

                statement.setInt(1, product.getCategoryId());
                statement.setString(2, product.getProductNumber());
                statement.setString(3, product.getSlug());
                statement.setString(4, product.getProductName());
                statement.setString(5, product.getShortDescription());
                statement.setString(6, product.getDescription());
                statement.setInt(7, product.getPrice());
                statement.setDouble(8, product.getVat());
                statement.setString(9, product.getImage());
                statement.setInt(10, product.getId());

                statement.executeUpdate();
                statement.close();
            }

            return product;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int delete(Product product) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM TS_PRODUCT WHERE ID=?");

            statement.setInt(1, product.getId());
            int deleted = statement.executeUpdate();
            statement.close();

            return deleted;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Product productMapper(ResultSet result, boolean entity) throws SQLException {
        Product product = new Product();
        product.setId(result.getInt("id"));
        product.setCategoryId(result.getInt("categoryid"));
        product.setProductName(result.getString("productname"));
        product.setSlug(result.getString("slug"));
        product.setPrice(result.getInt("price"));
        product.setImage(result.getString("image"));
        product.setCreatedAt(result.getTimestamp("createdat"));

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
