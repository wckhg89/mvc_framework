package next.dao;

import java.sql.SQLException;
import java.util.List;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import core.jdbc.SetParam;
import next.model.User;

public class UserDao {

    public void insert(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.saveOrUpdate("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.saveOrUpdate("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.queryForList(sql, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }

}
