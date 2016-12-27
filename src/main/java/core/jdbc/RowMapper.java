package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kanghonggu on 2016-12-27.
 */
public interface RowMapper <T> {
    T rowMap (ResultSet rs) throws SQLException;
}
