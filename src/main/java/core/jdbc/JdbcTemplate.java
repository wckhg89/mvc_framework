package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghonggu on 2016-12-27.
 */
public class JdbcTemplate {
    public void saveOrUpdate (String sql, SetParam setParam) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            setParam.setParam(pstmt);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    private <T> List<T> commonQuery (String sql, SetParam setParam, RowMapper<T> rowMapper) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> users = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            if (setParam != null) {
                setParam.setParam(pstmt);
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(rowMapper.rowMap(rs));
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return users;
    }

    public <T> T queryForObject (String sql, SetParam setParam, RowMapper<T> rowMapper) throws SQLException {
        List<T> users = commonQuery(sql, setParam, rowMapper);
        return users.size() == 0 ? null : users.get(0);
    }

    public <T> List<T> queryForList (String sql, RowMapper<T> rowMapper) throws SQLException {
        return commonQuery(sql, null, rowMapper);
    }
}
