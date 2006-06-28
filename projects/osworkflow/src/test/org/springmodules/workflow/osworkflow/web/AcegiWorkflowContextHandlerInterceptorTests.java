
package org.springmodules.workflow.osworkflow.web;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.userdetails.User;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author robh
 */
public class AcegiWorkflowContextHandlerInterceptorTests extends AbstractWorkflowContextHandlerInterceptorTests {


	protected MockHttpServletRequest getMockRequest(String userName) {
		User user = new User(userName, "dummy", true, true, true, true, new GrantedAuthority[]{});
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		return new MockHttpServletRequest();
	}

	protected AbstractWorkflowContextHandlerInterceptor getInterceptor() {
		return new AcegiWorkflowContextHandlerInterceptor();
	}
}
