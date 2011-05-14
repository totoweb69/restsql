/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.restsql.core.Config;

/**
 * Provides access to logs.
 * 
 * @author Mark Sawers
 */
@Path("log")
public class LogResource {
	private static final String LOG_NAME_ACCESS = "access.log";
	private static final String LOG_NAME_ERROR = "error.log";
	private static final String LOG_NAME_INTERNAL = "internal.log";
	private static final String LOG_NAME_TRACE = "trace.log";

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getLogList(@Context final UriInfo uriInfo) {
		String baseUri = uriInfo.getBaseUri().toString() + "log/";
		final StringBuffer body = new StringBuffer(500);
		body.append("<html>\n<body style=\"font-family:sans-serif\">\n");
		body.append("<span style=\"font-weight:bold\">Current Logs</span><br/>");
		appendCurrentLogAnchor(body, baseUri, "access");
		appendCurrentLogAnchor(body, baseUri, "error");
		appendCurrentLogAnchor(body, baseUri, "trace");
		appendCurrentLogAnchor(body, baseUri, "internal");
		body.append("<p/><p/><span style=\"font-weight:bold\">Historical Logs</span><br/>");
		final File dir = new File(getLogDir());
		for (final File file : dir.listFiles()) {
			if (file.getName().contains(".log")) {
				if (!file.getName().equals(LOG_NAME_ACCESS) && !file.getName().equals(LOG_NAME_ERROR)
						&& !file.getName().equals(LOG_NAME_TRACE)
						&& !file.getName().equals(LOG_NAME_INTERNAL)) {
					body.append("<a href=\"");
					body.append(baseUri);
					body.append(file.getName());
					body.append("\">");
					body.append(file.getName());
					body.append("</a><br/>");
				}
			}
		}
		body.append("</body>\n</html>");
		return Response.ok(body.toString()).build();
	}
	
	@GET
	@Path("access")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCurrentAccessLog() {
		return getFileContents(LOG_NAME_ACCESS);
	}

	@GET
	@Path("error")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCurrentErrorLog() {
		return getFileContents(LOG_NAME_ERROR);
	}

	@GET
	@Path("internal")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCurrentInternalLog() {
		return getFileContents(LOG_NAME_INTERNAL);
	}

	@GET
	@Path("trace")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getCurrentTraceLog() {
		return getFileContents(LOG_NAME_TRACE);
	}

	@GET
	@Path("{fileName}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getLog(@PathParam("fileName") final String fileName) {
		return getFileContents(fileName);
	}

	// Private utils

	private void appendCurrentLogAnchor(StringBuffer body, String baseUri, String log) {
		body.append("<a href=\"");
		body.append(baseUri);
		body.append(log);
		body.append("\">");
		body.append(log);
		body.append("</a><br/>");
	}

	private Response getFileContents(final String fileName) {
		try {
			final FileInputStream inputStream = new FileInputStream(getLogDir() + "/" + fileName);
			return Response.ok(inputStream).build();
		} catch (final FileNotFoundException exception) {
			return Response.status(Status.NOT_FOUND).entity(fileName + " not found").build();
		}
	}

	private String getLogDir() {
		return Config.properties.getProperty(Config.KEY_LOGGING_DIR, Config.DEFAULT_LOGGING_DIR);
	}
}
