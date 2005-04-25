
package org.springmodules.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;

import net.sf.acegisecurity.context.security.SecureContext;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.providers.dao.User;

/**
 * @author robh
 */
public class AcegiWorkflowContextHandlerInterceptor extends DefaultWorkflowContextHandlerInterceptor {

	protected String getCaller(HttpServletRequest request) {
		SecureContext context = (SecureContext)ContextHolder.getContext();
		User user = (User)context.getAuthentication().getPrincipal();
		return user.getUsername();
	}
}
