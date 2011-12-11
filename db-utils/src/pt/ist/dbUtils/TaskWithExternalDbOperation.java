package pt.ist.dbUtils;

import java.sql.SQLException;

public abstract class TaskWithExternalDbOperation extends TaskWithExternalDbOperation_Base {

    private static ThreadLocal<DbTransaction> transaction = new InheritableThreadLocal<DbTransaction>();

    private class EmbededExternalDbOperation extends ExternalDbOperation {

	private final TaskWithExternalDbOperation instance;

	public EmbededExternalDbOperation(final TaskWithExternalDbOperation instance) {
	    this.instance = instance;
	}

	@Override
	protected void doOperation() throws SQLException {
	    instance.doOperation();
	}

	@Override
	protected String getDbPropertyPrefix() {
	    return instance.getDbPropertyPrefix();
	}

	@Override
	protected void handleSQLException(final SQLException e) {
	    handle(e);
	}

    }

    @Override
    public void executeTask() {
	try {
	    final EmbededExternalDbOperation embededExternalDbOperation = new EmbededExternalDbOperation(this);
	    transaction.set(embededExternalDbOperation);
	    embededExternalDbOperation.execute();
	} finally {
	    transaction.remove();
	}
    }

    protected void executeQuery(final ExternalDbQuery externalDbQuery) {
	final DbTransaction dbTransaction = transaction.get();
	if (dbTransaction == null) {
	    throw new Error("error.not.inside.transaction");
	}
	System.out.println("Executing query: ");
	System.out.println(externalDbQuery.getQueryString());
	System.out.println();
	try {
	    dbTransaction.executeQuery(externalDbQuery);
	} catch (final SQLException e) {
	    handle(e, externalDbQuery);
	}
	System.out.println();
	System.out.println();
    }

    protected abstract String getDbPropertyPrefix();

    protected abstract void doOperation() throws SQLException;

    protected void handle(final SQLException e) {
	throw new Error(e);
    }

    protected void handle(final SQLException e, final ExternalDbQuery externalDbQuery) {
	handle(e);
    }

}
