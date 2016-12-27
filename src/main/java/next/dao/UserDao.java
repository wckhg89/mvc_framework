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
        SetParam setParam = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };
        jdbcTemplate.saveOrUpdate("INSERT INTO USERS VALUES (?, ?, ?, ?)", setParam);
    }

    public void update(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        SetParam setParam = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };
        jdbcTemplate.saveOrUpdate("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", setParam);
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        RowMapper rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.queryForList(sql, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        SetParam setParam = pstmt -> pstmt.setString(1, userId);
        RowMapper rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = ?";
        return (User) jdbcTemplate.queryForObject(sql, setParam, rowMapper);
    }

}
