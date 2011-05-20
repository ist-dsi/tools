package pt.ist.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import myorg._development.PropertiesManager;

public abstract class DbTransaction {

    private Connection connection = null;

    protected String getDatabaseUrl() {
	StringBuilder stringBuffer = new StringBuilder();
	stringBuffer.append("jdbc:oracle:thin:");
	stringBuffer.append(PropertiesManager.getProperty(getDbPropertyPrefix() + ".user"));
	stringBuffer.append("/");
	stringBuffer.append(PropertiesManager.getProperty(getDbPropertyPrefix() + ".pass"));
	stringBuffer.append("@");
	stringBuffer.append(PropertiesManager.getProperty(getDbPropertyPrefix() + ".alias"));
	return stringBuffer.toString();
    }

    protected abstract String getDbPropertyPrefix();

    public void executeQuery(final ExternalDbQuery externalDbQuery) throws SQLException {
	if (connection == null) {
	    openConnection();
	}
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	try {
	    preparedStatement = connection.prepareStatement(externalDbQuery.getQueryString());
	    resultSet = preparedStatement.executeQuery();
	    externalDbQuery.processResultSet(resultSet);
	} finally {
	    if (resultSet != null) {
		try {
		    resultSet.close();
		} catch (final SQLException e) {
		    e.printStackTrace();
		}
	    }
	    if (preparedStatement != null) {
		try {
		    preparedStatement.close();
		} catch (final SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    protected void openConnection() {
	if (connection != null) {
	    throw new Error("error.connection.already.open");
	}
	try {
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	    connection = DriverManager.getConnection(getDatabaseUrl());
	    connection.setAutoCommit(false);
	} catch (final SQLException e) {
	    throw new Error(e);
	}
    }

    protected void closeConnection() {
	if (connection != null) {
	    try {
		connection.close();
	    } catch (final SQLException e) {
		throw new Error(e);
	    } finally {
		connection = null;
	    }
	}
    }

    public void commit() {
	if (connection != null) {
	    try {
		connection.commit();
	    } catch (final SQLException e) {
		throw new Error(e);
	    } finally {
		closeConnection();
	    }
	}
    }

    public void abort() {
	if (connection != null) {
	    try {
		connection.rollback();
	    } catch (final SQLException e) {
		throw new Error(e);
	    } finally {
		closeConnection();
	    }
	}
    }

}
