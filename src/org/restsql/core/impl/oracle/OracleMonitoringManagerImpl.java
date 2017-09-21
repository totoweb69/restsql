package org.restsql.core.impl.oracle;

import org.restsql.core.Config;
import org.restsql.service.monitoring.MonitoringManagerImpl;

public class OracleMonitoringManagerImpl extends MonitoringManagerImpl {

	@Override
	protected void initHealthCheck() {
		healthCheckRegistry.register("database.connection", new OracleDatabaseConnectionHealthCheck());
		if (healthCheckRegistry.runHealthCheck("database.connection").isHealthy()) {
			Config.logger.info(String.format(
					"%s Initialized database connection health check and first check is healthy",
					MonitoringManagerImpl.class.getName()));
		} else {
			String message = String.format(
					"%s Initialized database connection health check and first check is unhealthy! See restsql internal log file for details.",
					MonitoringManagerImpl.class.getName());
			Config.logger.error(message);
			System.out.println(String.format("ERROR: %s", message));
		}
	}

}
