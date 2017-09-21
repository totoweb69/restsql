/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.core.impl.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.restsql.core.Config;
import org.restsql.core.Factory;
import org.restsql.service.monitoring.DatabaseConnectionHealthCheck;



/**
 * Pings configured database.
 * 
 * @author Mark Sawers
 */
public class OracleDatabaseConnectionHealthCheck extends DatabaseConnectionHealthCheck {
	@Override
	protected Result check() throws Exception {
		Connection connection = null;
		try {
			connection = Factory.getConnection(null);
			PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM DUAL ");
			statement.executeQuery();
			return Result.healthy();
		} catch (SQLException exception) {
			Config.logger.error("Database health check failure", exception);
			return Result.unhealthy("Can't ping database");
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
