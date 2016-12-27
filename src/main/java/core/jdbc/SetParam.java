package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by kanghonggu on 2016-12-27.
 */
public interface SetParam {
    void setParam (PreparedStatement pstmt) throws SQLException;
}
