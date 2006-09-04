
package org.springmodules.workflow.osworkflow.web;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author robh
 */
public class AcegiWorkflowContextHandlerInterceptorDifferentProviderTests extends AbstractWorkflowContextHandlerInterceptorTests {


	protected MockHttpServletRequest getMockRequest(String userName) {
		Authentication auth = new AnonymousAuthenticationToken(userName, userName, new GrantedAuthority[]{ new GrantedAuthorityImpl(userName) });
		SecurityContext context = new SecurityContextImpl();
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		return new MockHttpServletRequest();
	}
	
	protected AbstractWorkflowContextHandlerInterceptor getInterceptor() {
		return new AcegiWorkflowContextHandlerInterceptor();
	}
	
}
