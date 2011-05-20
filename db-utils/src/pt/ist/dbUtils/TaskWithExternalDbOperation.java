package pt.ist.dbUtils;

import java.sql.SQLException;

public abstract class TaskWithExternalDbOperation extends TaskWithExternalDbOperation_Base {

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
	
    }

    @Override
    public final void executeTask() {
	final EmbededExternalDbOperation embededExternalDbOperation = new EmbededExternalDbOperation(this);
	embededExternalDbOperation.execute();
    }

    protected abstract String getDbPropertyPrefix();

    protected abstract void doOperation() throws SQLException;

}
