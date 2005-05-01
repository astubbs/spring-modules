
package org.springmodules.workflow.osworkflow.web;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.context.ContextHolder;
import net.sf.acegisecurity.context.security.SecureContext;
import net.sf.acegisecurity.context.security.SecureContextImpl;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import net.sf.acegisecurity.providers.dao.User;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author robh
 */
public class AcegiWorkflowContextHandlerInterceptorTests extends AbstractWorkflowContextHandlerInterceptorTests {


	protected MockHttpServletRequest getMockRequest(String userName) {
		User user = new User(userName, "dummy", true, true, true, true, new GrantedAuthority[]{});
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
		SecureContext context = new SecureContextImpl();
		context.setAuthentication(auth);
		ContextHolder.setContext(context);

		return new MockHttpServletRequest();
	}

	protected AbstractWorkflowContextHandlerInterceptor getInterceptor() {
		return new AcegiWorkflowContextHandlerInterceptor();
	}
}
