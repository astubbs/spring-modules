
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

	private static final String INSTANCE_ID = "org.springmodules.workflow.osworkflow.instanceId";

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		Long instanceId = (Long) session.getAttribute(INSTANCE_ID);

		if (instanceId != null) {
			WorkflowContext.setInstanceId(instanceId.longValue());
		}
		filterChain.doFilter(request, response);

		if (WorkflowContext.hasInstanceId()) {
			session.setAttribute(INSTANCE_ID, new Long(WorkflowContext.getInstanceId()));
		}

	}
}
