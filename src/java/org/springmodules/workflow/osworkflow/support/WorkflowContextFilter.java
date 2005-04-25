
package org.springmodules.workflow.osworkflow.support;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springmodules.workflow.osworkflow.WorkflowContextManager;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author robh
 */
public class WorkflowContextFilter extends OncePerRequestFilter {

	public static final String DEFAULT_WORKFLOW_CONTEXT_MANAGER_BEAN = "workflowContextManager";

	public static final String DEFAULT_INSTANCE_ID_KEY = "org.springmodules.workflow.osworkflow.instanceId";

	private String instanceIdKey = DEFAULT_INSTANCE_ID_KEY;

	private String workflowContextManagerBeanName = DEFAULT_WORKFLOW_CONTEXT_MANAGER_BEAN;

	public void setWorkflowContextManagerBeanName(String workflowContextManagerBeanName) {
		this.workflowContextManagerBeanName = workflowContextManagerBeanName;
	}

	protected String getWorkflowContextManagerBeanName() {
		return this.workflowContextManagerBeanName;
	}

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		WorkflowContextManager manager = lookupWorkflowContextManager();

		HttpSession session = request.getSession();
		Long instanceId = (Long) session.getAttribute(instanceIdKey);

		if (instanceId != null) {
			manager.setInstanceId(instanceId.longValue());
		}
		filterChain.doFilter(request, response);

		if (manager.isInstanceIdBound()) {
			session.setAttribute(instanceIdKey, new Long(manager.getInstanceId()));
		}

	}

	protected WorkflowContextManager lookupWorkflowContextManager() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		return (WorkflowContextManager) wac.getBean(getWorkflowContextManagerBeanName());
	}
}
