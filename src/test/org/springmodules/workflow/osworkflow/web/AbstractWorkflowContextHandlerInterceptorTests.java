
package org.springmodules.workflow.osworkflow.web;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.springmodules.workflow.osworkflow.ThreadLocalWorkflowContextManager;
import org.springmodules.workflow.osworkflow.WorkflowContextManager;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author robh
 */
public abstract class AbstractWorkflowContextHandlerInterceptorTests extends TestCase {

	private static final String MOCK_CALLER = "robh";

	private static final Long MOCK_INSTANCE_ID = new Long(12);

	private MockControl mockControl;

	private WorkflowContextManager mockManager;

	private AbstractWorkflowContextHandlerInterceptor interceptor;

	private MockHttpServletRequest request;

	public void setUp() {
		this.mockControl = MockControl.createControl(WorkflowContextManager.class);
		this.mockManager = (WorkflowContextManager) mockControl.getMock();
		this.interceptor = getInterceptor();
		this.request = getMockRequest(MOCK_CALLER);
	}

	public void testPreHandleWithSessionStorageEnabled() throws Exception{
		// record WorkflowContextManager calls
		this.mockManager.setCaller(MOCK_CALLER);
		this.mockControl.setVoidCallable();
		this.mockManager.setInstanceId(MOCK_INSTANCE_ID.longValue());
		this.mockControl.setVoidCallable();
		this.mockControl.replay();

		// setup mock session
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(AbstractWorkflowContextHandlerInterceptor.SESSION_KEY_INSTANCE_ID, MOCK_INSTANCE_ID);
		this.request.setSession(session);

		this.interceptor.setContextManager(this.mockManager);

		this.interceptor.preHandle(this.request, new MockHttpServletResponse(), null);

		this.mockControl.verify();
	}

	public void testPreHandleWithSessionStorageDisabled() throws Exception {
		// record WorkflowContextManager calls
		this.mockManager.setCaller(MOCK_CALLER);
		this.mockControl.setVoidCallable();
		this.mockControl.replay();

		this.interceptor.setSessionStorageEnabled(false);
		this.interceptor.setContextManager(this.mockManager);
		this.interceptor.preHandle(this.request, new MockHttpServletResponse(), null);

		this.mockControl.verify();
	}

	public void testPostHandleWithSessionStorageEnabled() throws Exception {
		// record WorkflowContextManager calls
		this.mockManager.getInstanceId();
		this.mockControl.setReturnValue(MOCK_INSTANCE_ID.longValue());
		this.mockControl.replay();

		MockHttpSession session = new MockHttpSession();
		this.request.setSession(session);

		this.interceptor.setContextManager(this.mockManager);
		this.interceptor.postHandle(this.request, new MockHttpServletResponse(), null, null);

		mockControl.verify();

		assertEquals(session.getAttribute(AbstractWorkflowContextHandlerInterceptor.SESSION_KEY_INSTANCE_ID), MOCK_INSTANCE_ID);
	}

	public void testPostHandleWithSessionStorageDisabled() throws Exception {
		// record WorkflowContextManager calls
		this.mockControl.replay();

		MockHttpSession session = new MockHttpSession();
		this.request.setSession(session);

		this.interceptor.setSessionStorageEnabled(false);
		this.interceptor.setContextManager(this.mockManager);
		this.interceptor.postHandle(this.request, new MockHttpServletResponse(), null, null);

		this.mockControl.verify();
		assertNull(session.getAttribute(AbstractWorkflowContextHandlerInterceptor.SESSION_KEY_INSTANCE_ID));
	}

	protected abstract MockHttpServletRequest getMockRequest(String userName);

	protected abstract AbstractWorkflowContextHandlerInterceptor getInterceptor();
}
