/* Copyright (c) restSQL Project Contributors. Licensed under MIT. */
package org.restsql.service;

import java.security.Principal;

import org.restsql.security.SecurityContext;
import org.restsql.security.SqlResourceRolePrivileges;


/**
 * Adapts JAX-RS's SecurityContext to restSQL's SecurityContext .
 * 
 * @author Mark Sawers
 */
public class SecurityContextAdapter implements SecurityContext {
	private final javax.ws.rs.core.SecurityContext jaxRsContext;

	public SecurityContextAdapter(javax.ws.rs.core.SecurityContext jaxRsContext) {
		this.jaxRsContext = jaxRsContext;
	}
	
        @Override
	public Principal getUserPrincipal() {
		return jaxRsContext.getUserPrincipal();
	}

        @Override
	public boolean isUserInRole(String roleName) {
                if(SqlResourceRolePrivileges.TOKEN_WILDCARD.equals(roleName)){
                    return true;
                }
		return jaxRsContext.isUserInRole(roleName);
	}
}
