
package org.springmodules.workflow.osworkflow.web;

import java.security.Principal;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author robh
 */
public class DefaultWorkflowContextHandlerInterceptorTests extends AbstractWorkflowContextHandlerInterceptorTests {


	protected MockHttpServletRequest getMockRequest(String userName) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setUserPrincipal(new MockPrincipal(userName));
		return request;
	}

	protected AbstractWorkflowContextHandlerInterceptor getInterceptor() {
		return new DefaultWorkflowContextHandlerInterceptor();
	}

	private static class MockPrincipal implements Principal {

		private String name;

		public MockPrincipal(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
}
