
package org.springmodules.examples.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springmodules.workflow.osworkflow.WorkflowContextManager;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author robh
 */
public class CallerHandlerInterceptor extends HandlerInterceptorAdapter {

	private WorkflowContextManager contextManager;

	public void setContextManager(WorkflowContextManager contextManager) {
		this.contextManager = contextManager;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if(request.getUserPrincipal() != null) {
			  this.contextManager.setCaller(request.getUserPrincipal().getName());
		}

		return true;
	}
}
