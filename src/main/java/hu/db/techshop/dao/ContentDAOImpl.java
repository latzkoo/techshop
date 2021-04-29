package hu.db.techshop.dao;

import hu.db.techshop.model.Content;
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
public class ContentDAOImpl extends JdbcDaoSupport implements ContentDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public List<Content> findAll() {
        String query = "SELECT * FROM TS_CONTENT ORDER BY TITLE";
        return jdbcTemplate.query(query, (row, i) -> contentMapper(row));
    }

    @Override
    public Content findById(int id) {
        try {
            String query = "SELECT * FROM TS_CONTENT WHERE ID=?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Content content = contentMapper(result);
                statement.close();

                return content;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Content save(Content content) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE TS_CONTENT SET TITLE=?, CONTENT=? WHERE ID=?");
            statement.setString(1, content.getTitle());
            statement.setString(2, content.getContent());
            statement.setInt(3, content.getId());

            statement.executeUpdate();
            statement.close();

            return content;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Content contentMapper(ResultSet result) throws SQLException {
        Content content = new Content();
        content.setId(result.getInt("id"));
        content.setTitle(result.getString("title"));
        content.setContent(result.getString("content"));
        content.setCreatedAt(result.getTimestamp("createdat"));

        return content;
    }

}
