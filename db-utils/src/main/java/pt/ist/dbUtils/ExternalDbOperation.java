package pt.ist.dbUtils;

import java.sql.SQLException;

public abstract class ExternalDbOperation extends DbTransaction {

    protected abstract void doOperation() throws SQLException;

    protected void handleSQLException(final SQLException e) {
        throw new Error(e);
    }

    public void execute() {
        boolean successful = false;
        try {
            doOperation();
            commit();
            successful = true;
        } catch (final SQLException e) {
            handleSQLException(e);
        } finally {
            if (!successful) {
                abort();
            }
        }
    }

}
