package hu.db.techshop.dao;

import hu.db.techshop.model.Comment;
import hu.db.techshop.model.User;
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
public class CommentDAOImpl extends JdbcDaoSupport implements CommentDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Comment> findAll(int productId) {
        String query = "SELECT C.*, U.FIRSTNAME, U.LASTNAME " +
                "FROM TS_COMMENT C, TS_USER U " +
                "WHERE C.USERID=U.ID " +
                "AND C.PRODUCTID = ? " +
                "ORDER BY C.CREATEDAT DESC";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, productId);
        }, (row, i) -> commentMapper(row));
    }

    @Override
    public Comment findById(int id) {
        try {
            String query = "SELECT C.*, U.FIRSTNAME, U.LASTNAME " +
                    "FROM TS_COMMENT C, TS_USER U " +
                    "WHERE C.USERID=U.ID " +
                    "AND C.ID = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Comment comment = commentMapper(result);
                statement.close();

                return comment;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Comment save(Comment comment) {
        try {
            // Insert
            if (comment.getId() <= 0) {
                final String SQL = "INSERT INTO TS_COMMENT (ID, USERID, PRODUCTID, COMMENTTEXT) " +
                        "VALUES (TS_COMMENT_SEQ.nextval,?,?,?)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL, new String[] {"ID"});
                    statement.setInt(1, comment.getUserId());
                    statement.setInt(2, comment.getProductId());
                    statement.setString(3, comment.getComment());

                    return statement;
                }, keyHolder);

                if (keyHolder.getKey() != null) {
                    comment.setId(keyHolder.getKey().intValue());
                }
            }
            // Update
            else {
                PreparedStatement statement = getConnection().prepareStatement(
                        "UPDATE TS_COMMENT SET COMMENTTEXT=? WHERE ID=?");

                statement.setString(1, comment.getComment());
                statement.setInt(2, comment.getId());

                statement.executeUpdate();
                statement.close();
            }

            return comment;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(Comment comment) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM TS_COMMENT WHERE ID=?");

            statement.setInt(1, comment.getId());
            statement.executeUpdate();

            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Comment commentMapper(ResultSet result) throws SQLException {
        Comment comment = new Comment();
        comment.setId(result.getInt("id"));
        comment.setUserId(result.getInt("userid"));
        comment.setProductId(result.getInt("productid"));
        comment.setComment(result.getString("commenttext"));
        comment.setCreatedAt(result.getTimestamp("createdat"));
        comment.setUser(new User(comment.getId(), result.getString("firstname"), result.getString("lastname")));

        return comment;
    }

}
