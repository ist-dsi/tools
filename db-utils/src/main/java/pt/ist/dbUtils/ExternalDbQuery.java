package pt.ist.dbUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ExternalDbQuery {

    public String getQueryString();

    public void processResultSet(final ResultSet resultSet) throws SQLException;

}
