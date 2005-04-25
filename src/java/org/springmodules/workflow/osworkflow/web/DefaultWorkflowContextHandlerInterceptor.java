
package org.springmodules.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;

/**
 * @author robh
 */
public class DefaultWorkflowContextHandlerInterceptor extends AbstractWorkflowContextHandlerInterceptor {

	protected String getCaller(HttpServletRequest request) {
		return (request.getUserPrincipal() == null) ? null : request.getUserPrincipal().getName();
	}
}
