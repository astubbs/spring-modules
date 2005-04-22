
package org.springmodules.workflow.osworkflow.support;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springmodules.workflow.osworkflow.WorkflowContext;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author robh
 */
public class WorkflowContextFilter extends OncePerRequestFilter {

	public static final String DEFAULT_INSTANCE_ID_KEY = "org.springmodules.workflow.osworkflow.instanceId";

	private String instanceIdKey = DEFAULT_INSTANCE_ID_KEY;

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		Long instanceId = (Long) session.getAttribute(instanceIdKey);

		if (instanceId != null) {
			WorkflowContext.setInstanceId(instanceId.longValue());
		}
		filterChain.doFilter(request, response);

		if (WorkflowContext.hasInstanceId()) {
			session.setAttribute(instanceIdKey, new Long(WorkflowContext.getInstanceId()));
		}

	}
}
