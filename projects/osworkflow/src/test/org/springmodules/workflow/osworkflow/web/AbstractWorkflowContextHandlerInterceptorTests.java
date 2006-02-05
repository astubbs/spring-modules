
package org.springmodules.workflow.osworkflow.web;

import junit.framework.TestCase;
import org.springmodules.workflow.osworkflow.OsWorkflowContext;
import org.springmodules.workflow.osworkflow.OsWorkflowContextHolder;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

/**
 * @author robh
 */
public abstract class AbstractWorkflowContextHandlerInterceptorTests extends TestCase {


	private static final String MOCK_CALLER = "robh";

	private static final Long MOCK_INSTANCE_ID = new Long(12);

	private AbstractWorkflowContextHandlerInterceptor interceptor;

	private MockHttpServletRequest request;

	private OsWorkflowContext context;

	public void setUp() {
		OsWorkflowContextHolder.clearWorkflowContext();
		this.context = OsWorkflowContextHolder.getWorkflowContext();
		this.request = getMockRequest(MOCK_CALLER);
		this.interceptor = getInterceptor();
	}

	public void testPreHandleWithSessionStorageEnabled() throws Exception {
		// setup mock session
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(AbstractWorkflowContextHandlerInterceptor.SESSION_KEY_INSTANCE_ID, MOCK_INSTANCE_ID);
		this.request.setSession(session);

		this.interceptor.preHandle(this.request, new MockHttpServletResponse(), null);

		assertEquals("Caller not set", MOCK_CALLER, this.context.getCaller());
		assertEquals("Instance ID not set", MOCK_INSTANCE_ID.longValue(), this.context.getInstanceId());
	}

	public void testPreHandleWithSessionStorageDisabled() throws Exception {
		this.interceptor.setSessionStorageEnabled(false);
		this.interceptor.preHandle(this.request, new MockHttpServletResponse(), null);

		assertEquals(MOCK_CALLER, this.context.getCaller());
		assertFalse(this.context.hasInstanceId());
	}

	public void testPostHandleWithSessionStorageEnabled() throws Exception {
		this.context.setInstanceId(MOCK_INSTANCE_ID.longValue());

		MockHttpSession session = new MockHttpSession();
		this.request.setSession(session);

		this.interceptor.postHandle(this.request, new MockHttpServletResponse(), null, null);

		assertEquals(session.getAttribute(AbstractWorkflowContextHandlerInterceptor.SESSION_KEY_INSTANCE_ID), MOCK_INSTANCE_ID);
	}

	public void testPostHandleWithSessionStorageDisabled() throws Exception {
		this.context.setInstanceId(MOCK_INSTANCE_ID.longValue());

		MockHttpSession session = new MockHttpSession();
		this.request.setSession(session);

		this.interceptor.setSessionStorageEnabled(false);
		this.interceptor.postHandle(this.request, new MockHttpServletResponse(), null, null);

		assertNull(session.getAttribute(AbstractWorkflowContextHandlerInterceptor.SESSION_KEY_INSTANCE_ID));
	}

	protected abstract MockHttpServletRequest getMockRequest(String userName);

	protected abstract AbstractWorkflowContextHandlerInterceptor getInterceptor();
}
