
package org.springmodules.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import org.springmodules.workflow.osworkflow.ThreadLocalWorkflowContextManager;

/**
 * @author robh
 */
public abstract class AbstractWorkflowContextHandlerInterceptorTests extends TestCase {

	public void testSimple() {
		HttpServletRequest request = getRequest("robh");
		AbstractWorkflowContextHandlerInterceptor interceptor = getInterceptor();

		// TODO: change to mock
		interceptor.setContextManager(new ThreadLocalWorkflowContextManager());

		//interceptor.preHandle()
	}

	protected abstract HttpServletRequest getRequest(String userName);
	protected abstract AbstractWorkflowContextHandlerInterceptor getInterceptor();
}
