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
    private SetParam createPrepareStatement (Object... args) throws SQLException {
        return pstmt -> {
            for (int i = 1 ; i <= args.length ; i++) {
                pstmt.setObject(i, args[i-1]);
            }
        };
    }

    public void saveOrUpdate (String sql, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            SetParam setParam = this.createPrepareStatement(args);
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

    private <T> List<T> commonQuery (String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> users = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            SetParam setParam = this.createPrepareStatement(args);
            setParam.setParam(pstmt);
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

    public <T> T queryForObject (String sql,RowMapper<T> rowMapper,  Object... args) throws SQLException {
        List<T> users = commonQuery(sql, rowMapper, args);
        return users.size() == 0 ? null : users.get(0);
    }

    public <T> List<T> queryForList (String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        return commonQuery(sql, rowMapper, args);
    }
}
