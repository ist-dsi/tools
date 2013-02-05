package pt.ist.dbUtils;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ExternalDbCall extends ExternalDbQuery {

    public void prepareCall(final CallableStatement callableStatement) throws SQLException;

    public void processResultSet(final ResultSet resultSet, final CallableStatement callableStatement) throws SQLException;

}
